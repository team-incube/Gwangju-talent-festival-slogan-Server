package team.incube.gwangjutalentfestivalserver.domain.auth.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.incube.gwangjutalentfestivalserver.domain.auth.dto.AdminLoginRequest;
import team.incube.gwangjutalentfestivalserver.domain.auth.dto.AdminLoginResponse;
import team.incube.gwangjutalentfestivalserver.domain.user.entity.User;
import team.incube.gwangjutalentfestivalserver.domain.user.repository.UserRepository;
import team.incube.gwangjutalentfestivalserver.global.exception.HttpException;
import team.incube.gwangjutalentfestivalserver.global.security.jwt.JwtProvider;
import team.incube.gwangjutalentfestivalserver.global.security.jwt.dto.JwtDetails;

@Service
@Transactional
@RequiredArgsConstructor
public class LoginAdminUsecase {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtProvider jwtProvider;

	public AdminLoginResponse execute(AdminLoginRequest adminLoginRequest) {
		String phoneNumber = adminLoginRequest.getPhoneNumber();
		User user = userRepository.findByPhoneNumber(phoneNumber)
				.orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "해당 회원을 찾을 수 없습니다."));

		String rawPassword = adminLoginRequest.getPassword();
		String encodedPassword = user.getEncodedPassword();

		if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
			throw new HttpException(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다.");
		}

		JwtDetails accessToken = jwtProvider.generateToken(user.getId());

		return AdminLoginResponse.builder()
			.accessToken(accessToken.getToken())
			.accessTokenExpiredAt(accessToken.getExpiredAt())
			.build();
	}
}
