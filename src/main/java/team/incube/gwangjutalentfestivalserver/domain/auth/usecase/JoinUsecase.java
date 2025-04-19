package team.incube.gwangjutalentfestivalserver.domain.auth.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.incube.gwangjutalentfestivalserver.domain.auth.dto.request.SignUpRequest;
import team.incube.gwangjutalentfestivalserver.domain.user.entity.User;
import team.incube.gwangjutalentfestivalserver.domain.user.enums.Role;
import team.incube.gwangjutalentfestivalserver.domain.user.repository.UserRepository;
import team.incube.gwangjutalentfestivalserver.global.exception.HttpException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JoinUsecase {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	public void execute(SignUpRequest request) {
		String encodedPassword = passwordEncoder.encode(request.getPassword());

		if (userRepository.existsByEmail(request.getEmail())) {
			throw new HttpException(HttpStatus.BAD_REQUEST, "해당 이메일은 이미 사용중입니다.");
		}

		User user = User.builder()
				.email(request.getEmail())
				.encodedPassword(encodedPassword)
				.roles(List.of(Role.ROLE_USER))
				.build();

		userRepository.save(user);
	}
}
