package team.incube.gwangjutalentfestivalserver.domain.auth.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.incube.gwangjutalentfestivalserver.domain.auth.entity.RefreshToken;
import team.incube.gwangjutalentfestivalserver.domain.auth.repository.RefreshTokenRepository;
import team.incube.gwangjutalentfestivalserver.global.exception.HttpException;
import team.incube.gwangjutalentfestivalserver.global.security.jwt.JwtProvider;

@Service
@Transactional
@RequiredArgsConstructor
public class LogoutUsecase {

	private final RefreshTokenRepository refreshTokenRepository;
	private final JwtProvider jwtProvider;

	public void execute(String resolveRefreshToken) {
		RefreshToken savedRefreshToken = jwtProvider.getSavedRefreshTokenByRefreshToken(resolveRefreshToken);

		if (!resolveRefreshToken.equals(savedRefreshToken.getRefreshToken())) {
			throw new HttpException(HttpStatus.FORBIDDEN, "올바르지 않은 리프레시 토큰입니다.");
		}

		refreshTokenRepository.delete(savedRefreshToken);
	}
}
