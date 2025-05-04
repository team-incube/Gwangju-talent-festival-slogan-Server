package team.incube.gwangjutalentfestivalserver.global.security.details;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import team.incube.gwangjutalentfestivalserver.domain.user.entity.User;
import team.incube.gwangjutalentfestivalserver.domain.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import team.incube.gwangjutalentfestivalserver.global.exception.HttpException;

@Service
@RequiredArgsConstructor
public class AuthDetailsService implements UserDetailsService {
	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) {
		Long id = Long.parseLong(username);
		User userByEmail =
			userRepository.findById(id).orElseThrow(() ->
				new HttpException(HttpStatus.NOT_FOUND, "해당 회원을 찾을 수 없습니다.")
			);
		return new AuthDetails(userByEmail);
	}
}
