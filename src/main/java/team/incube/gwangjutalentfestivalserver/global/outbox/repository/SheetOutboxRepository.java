package team.incube.gwangjutalentfestivalserver.global.outbox.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import team.incube.gwangjutalentfestivalserver.global.outbox.entity.SheetOutbox;
import team.incube.gwangjutalentfestivalserver.global.outbox.enums.SheetOutboxStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface SheetOutboxRepository extends JpaRepository<SheetOutbox, Long> {

    List<SheetOutbox> findByStatusInAndNextRetryAtLessThanEqualOrderByIdAsc(
            List<SheetOutboxStatus> statuses,
            LocalDateTime now,
            Pageable pageable
    );
}
