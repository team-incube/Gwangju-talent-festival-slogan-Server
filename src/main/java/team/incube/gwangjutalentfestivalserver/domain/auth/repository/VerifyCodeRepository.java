package team.incube.gwangjutalentfestivalserver.domain.auth.repository;

import org.springframework.data.repository.CrudRepository;
import team.incube.gwangjutalentfestivalserver.domain.auth.entity.RefreshToken;
import team.incube.gwangjutalentfestivalserver.domain.auth.entity.VerifyCode;

import java.util.Optional;
import java.util.UUID;

public interface VerifyCodeRepository extends CrudRepository<VerifyCode, String> {
    Optional<VerifyCode> findByPhoneNumber(String phoneNumber);
}
