package team.incube.gwangjutalentfestivalserver.global.security.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtDetails {
	private String token;
	private LocalDateTime expiredAt;
}