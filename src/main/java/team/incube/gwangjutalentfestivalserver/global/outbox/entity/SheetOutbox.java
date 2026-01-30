package team.incube.gwangjutalentfestivalserver.global.outbox.entity;

import jakarta.persistence.*;
import lombok.*;
import team.incube.gwangjutalentfestivalserver.domain.slogan.enums.SheetType;
import team.incube.gwangjutalentfestivalserver.global.outbox.enums.SheetOutboxStatus;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "sheet_outbox",
        indexes = {
                @Index(name = "idx_sheet_outbox_ready", columnList = "status,nextRetryAt,id")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class SheetOutbox {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SheetOutboxStatus status;

    @Column(nullable = false)
    private int retryCount;

    @Column(nullable = false)
    private LocalDateTime nextRetryAt;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(columnDefinition = "TEXT")
    private String lastError;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String payloadJson;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SheetType sheetType;

    public void markProcessing() {
        this.status = SheetOutboxStatus.PROCESSING;
    }

    public void markDone() {
        this.status = SheetOutboxStatus.DONE;
        this.lastError = null;
    }

    public void markFailed(String error, LocalDateTime nextRetryAt) {
        this.status = SheetOutboxStatus.FAILED;
        this.retryCount++;
        this.lastError = error;
        this.nextRetryAt = nextRetryAt;
    }
}
