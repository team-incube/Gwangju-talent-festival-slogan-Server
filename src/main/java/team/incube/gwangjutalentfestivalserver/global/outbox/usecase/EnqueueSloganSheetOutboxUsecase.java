package team.incube.gwangjutalentfestivalserver.global.outbox.usecase;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.incube.gwangjutalentfestivalserver.domain.slogan.dto.request.SubmitSloganRequest;
import team.incube.gwangjutalentfestivalserver.domain.slogan.enums.SheetType;
import team.incube.gwangjutalentfestivalserver.global.outbox.dto.SloganSheetRowPayload;
import team.incube.gwangjutalentfestivalserver.global.outbox.entity.SheetOutbox;
import team.incube.gwangjutalentfestivalserver.global.outbox.enums.SheetOutboxStatus;
import team.incube.gwangjutalentfestivalserver.global.outbox.repository.SheetOutboxRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EnqueueSloganSheetOutboxUsecase {

    private final SheetOutboxRepository sheetOutboxRepository;
    private final ObjectMapper objectMapper;

    public void execute(
            SubmitSloganRequest request,
            Long sloganId,
            SheetType sheetType
    ) {
        try {
            SloganSheetRowPayload payload = new SloganSheetRowPayload(
                    sloganId,
                    request.getSlogan(),
                    request.getDescription(),
                    request.getSchool(),
                    request.getGrade(),
                    request.getClassroom(),
                    request.getName(),
                    request.getPhoneNumber()
            );

            SheetOutbox outbox = SheetOutbox.builder()
                    .status(SheetOutboxStatus.PENDING)
                    .retryCount(0)
                    .nextRetryAt(LocalDateTime.now())
                    .createdAt(LocalDateTime.now())
                    .payloadJson(objectMapper.writeValueAsString(payload))
                    .sheetType(sheetType)
                    .build();

            sheetOutboxRepository.save(outbox);

        } catch (Exception e) {
            throw new IllegalStateException("Failed to enqueue sheet outbox", e);
        }
    }
}