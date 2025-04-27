package team.incube.gwangjutalentfestivalserver.domain.auth.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import team.incube.gwangjutalentfestivalserver.domain.auth.repository.VerifyCountRepository;

@Component
@RequiredArgsConstructor
public class AuthScheduler {
    private final VerifyCountRepository verifyCountRepository;

    @Scheduled(cron = "0 0 0 * * *")
    public void resetVerifyCount(){
        verifyCountRepository.deleteAll();
    }
}
