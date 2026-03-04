package team.incube.gwangjutalentfestivalserver.global.outbox.usecase;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.incube.gwangjutalentfestivalserver.domain.slogan.enums.SheetType;
import team.incube.gwangjutalentfestivalserver.global.outbox.dto.SloganSheetRowPayload;
import team.incube.gwangjutalentfestivalserver.global.outbox.entity.SheetOutbox;
import team.incube.gwangjutalentfestivalserver.global.outbox.enums.SheetOutboxStatus;
import team.incube.gwangjutalentfestivalserver.global.outbox.repository.SheetOutboxRepository;
import team.incube.gwangjutalentfestivalserver.global.thirdparty.google.adapter.GoogleSheetsAdapter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProcessSheetOutboxUsecase {

    private final SheetOutboxRepository sheetOutboxRepository;
    private final GoogleSheetsAdapter googleSheetsAdapter;
    private final ObjectMapper objectMapper;

    private static final int BATCH_SIZE = 50;

    @Scheduled(fixedDelay = 1000)
    public void execute() {
        processSheetType(SheetType.PRIVATE);
        processSheetType(SheetType.PUBLIC);
    }

    private void processSheetType(SheetType sheetType) {
        List<SheetOutbox> batch = pickAndMarkProcessing(sheetType);

        if (batch.isEmpty()) {
            return;
        }

        try {
            List<List<Object>> rows = buildRows(batch, sheetType);

            googleSheetsAdapter.appendRows(rows, sheetType);

            markDone(batch);
        } catch (Exception e) {
            markFailed(batch, e);
        }
    }

    @Transactional
    protected List<SheetOutbox> pickAndMarkProcessing(SheetType sheetType) {
        LocalDateTime now = LocalDateTime.now();

        List<SheetOutboxStatus> targetStatuses =
                List.of(SheetOutboxStatus.PENDING, SheetOutboxStatus.FAILED);

        List<SheetOutbox> readyOutboxes =
                sheetOutboxRepository.findByStatusInAndNextRetryAtLessThanEqualOrderByIdAsc(
                        targetStatuses,
                        now,
                        PageRequest.of(0, BATCH_SIZE)
                );

        List<SheetOutbox> batch = new ArrayList<>();
        for (SheetOutbox outbox : readyOutboxes) {
            if (outbox.getSheetType() == sheetType) {
                outbox.markProcessing();
                batch.add(outbox);
            }
        }

        return batch;
    }

    private List<List<Object>> buildRows(List<SheetOutbox> batch, SheetType sheetType) throws Exception {
        List<List<Object>> rows = new ArrayList<>();

        for (SheetOutbox outbox : batch) {
            SloganSheetRowPayload payload =
                    objectMapper.readValue(outbox.getPayloadJson(), SloganSheetRowPayload.class);

            if (sheetType == SheetType.PUBLIC) {
                rows.add(List.of(
                        payload.getSloganId(),
                        payload.getSlogan(),
                        payload.getDescription()
                ));
            } else {
                rows.add(List.of(
                        payload.getSloganId(),
                        payload.getSchool(),
                        String.valueOf(payload.getGrade()),
                        String.valueOf(payload.getClassroom()),
                        payload.getName(),
                        payload.getPhoneNumber(),
                        payload.getSlogan(),
                        payload.getDescription()
                ));
            }
        }

        return rows;
    }

    @Transactional
    protected void markDone(List<SheetOutbox> batch) {
        for (SheetOutbox outbox : batch) {
            outbox.markDone();
        }
    }

    @Transactional
    protected void markFailed(List<SheetOutbox> batch, Exception e) {
        LocalDateTime nextRetryAt = LocalDateTime.now().plusSeconds(30);
        String error = truncate(e.getMessage());

        for (SheetOutbox outbox : batch) {
            outbox.markFailed(error, nextRetryAt);
        }
    }

    private String truncate(String message) {
        if (message == null) {
            return null;
        }
        return message.length() > 2000 ? message.substring(0, 2000) : message;
    }
}