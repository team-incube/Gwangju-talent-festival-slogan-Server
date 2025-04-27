package team.incube.gwangjutalentfestivalserver.domain.auth.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("verify-code")
public class VerifyCode {
	@Id
	private String phoneNumber;

	private String code;

	@TimeToLive(unit = TimeUnit.MILLISECONDS)
	private Long expires;
}
