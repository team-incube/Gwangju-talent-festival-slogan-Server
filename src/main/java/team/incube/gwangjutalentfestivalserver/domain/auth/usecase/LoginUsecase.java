package team.incube.gwangjutalentfestivalserver.domain.auth.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.incube.gwangjutalentfestivalserver.domain.auth.dto.request.SignInRequest;
import team.incube.gwangjutalentfestivalserver.domain.auth.dto.response.SignInResponse;
import team.incube.gwangjutalentfestivalserver.domain.auth.entity.RefreshToken;
import team.incube.gwangjutalentfestivalserver.domain.auth.repository.RefreshTokenRepository;
import team.incube.gwangjutalentfestivalserver.domain.user.entity.User;
import team.incube.gwangjutalentfestivalserver.domain.user.repository.UserRepository;
import team.incube.gwangjutalentfestivalserver.global.exception.HttpException;
import team.incube.gwangjutalentfestivalserver.global.security.jwt.JwtProvider;
import team.incube.gwangjutalentfestivalserver.global.security.jwt.JwtType;
import team.incube.gwangjutalentfestivalserver.global.security.jwt.dto.JwtDetails;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class LoginUsecase {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtProvider jwtProvider;
	private final RefreshTokenRepository refreshTokenRepository;

	public SignInResponse execute(SignInRequest signInRequest) {
		String email = signInRequest.getEmail();
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "해당 회원을 찾을 수 없습니다."));

		UUID id = user.getId();
		if (id == null) {
			throw new IllegalArgumentException("id cannot be null");
		}

		String rawPassword = signInRequest.getPassword();
		String encodedPassword = user.getEncodedPassword();

		if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
			throw new HttpException(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다.");
		}

		JwtDetails accessToken = jwtProvider.generateToken(id.toString(), JwtType.ACCESS_TOKEN);
		JwtDetails refreshToken = getRefreshTokenOrSave(id);

		return new SignInResponse(
				accessToken.getToken(),
				accessToken.getExpiredAt(),
				refreshToken.getToken(),
				refreshToken.getExpiredAt()
		);
	}

	public JwtDetails getRefreshTokenOrSave(UUID id) {
		Optional<RefreshToken> refreshTokenOptional = refreshTokenRepository.findById(id);

		if (refreshTokenOptional.isEmpty()) {
			JwtDetails newRefreshToken = jwtProvider.generateToken(id.toString(), JwtType.REFRESH_TOKEN);
			RefreshToken tokenEntity = new RefreshToken(id, newRefreshToken.getToken(), jwtProvider.getRefreshTokenExpires());
			refreshTokenRepository.save(tokenEntity);
			return newRefreshToken;
		} else {
			RefreshToken existing = refreshTokenOptional.get();
			return new JwtDetails(
					existing.getRefreshToken(),
					LocalDateTime.now().plus(Duration.ofMillis(existing.getExpires()))
			);
		}
	}
}
