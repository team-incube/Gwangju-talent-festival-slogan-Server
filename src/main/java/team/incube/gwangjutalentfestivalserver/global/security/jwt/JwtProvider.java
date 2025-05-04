package team.incube.gwangjutalentfestivalserver.global.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import team.incube.gwangjutalentfestivalserver.global.exception.HttpException;
import team.incube.gwangjutalentfestivalserver.global.security.details.AuthDetailsService;
import team.incube.gwangjutalentfestivalserver.global.security.jwt.dto.JwtDetails;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtProvider {
	private final AuthDetailsService authDetailsService;
	private final String accessTokenKey;
	private final long accessTokenExpires;

	public JwtProvider(
			AuthDetailsService authDetailsService,
			@Value("${jwt.access-token-key}") String accessTokenKey,
			@Value("${jwt.access-token-expires}") long accessTokenExpires
	) {
		this.authDetailsService = authDetailsService;
		this.accessTokenKey = accessTokenKey;
		this.accessTokenExpires = accessTokenExpires;
	}

	public UsernamePasswordAuthenticationToken getAuthentication(String token) {
		String resolvedToken = resolveToken(token);
		Claims payload = getPayload(resolvedToken);

		var userDetails = authDetailsService.loadUserByUsername(payload.getSubject());

		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	}

	public String resolveToken(String token) {
		if (token == null || !token.startsWith("Bearer ")) {
			return null;
		} else {
			return token.substring(7);
		}
	}

	public Claims getPayload(String token) {
		if (token == null) {
			throw new HttpException(HttpStatus.FORBIDDEN, "토큰이 비어있습니다.");
		}

		byte[] keyBytes = Base64.getEncoder().encode(accessTokenKey.getBytes());
		var signingKey = Keys.hmacShaKeyFor(keyBytes);

		try {
			return Jwts
					.parser()
					.verifyWith(signingKey)
					.build()
					.parseSignedClaims(token)
					.getPayload();
		} catch (ExpiredJwtException e) {
			throw new HttpException(HttpStatus.FORBIDDEN, "만료된 토큰입니다.");
		} catch (UnsupportedJwtException e) {
			throw new HttpException(HttpStatus.FORBIDDEN, "지원하지 않는 형식의 토큰입니다.");
		} catch (MalformedJwtException e) {
			throw new HttpException(HttpStatus.FORBIDDEN, "올바르지 않은 토큰입니다.");
		} catch (RuntimeException e) {
			throw new HttpException(HttpStatus.FORBIDDEN, "JWT 인증 과정에서 문제가 생겼습니다.");
		}
	}

	public JwtDetails generateToken(Long id) {
		LocalDateTime expiredAt = LocalDateTime.now().plus(
				Duration.ofMillis(accessTokenExpires)
		);

		byte[] keyBytes = Base64.getEncoder().encode(accessTokenKey.getBytes());
		var signingKey = Keys.hmacShaKeyFor(keyBytes);

		Date expiration = Date.from(expiredAt.atZone(ZoneId.of("Asia/Seoul")).toInstant());

		String token = Jwts.builder()
				.subject(id.toString())
				.signWith(signingKey)
				.issuedAt(new Date())
				.expiration(expiration)
				.compact();

		return new JwtDetails(token, expiredAt);
	}

}
