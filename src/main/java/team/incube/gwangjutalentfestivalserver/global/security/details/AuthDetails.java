package team.incube.gwangjutalentfestivalserver.global.security.details;

import lombok.RequiredArgsConstructor;
import team.incube.gwangjutalentfestivalserver.domain.user.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class AuthDetails implements UserDetails {
	private final User user;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(
			new SimpleGrantedAuthority(user.getRole().name())
		);
	}

	@Override
	public String getPassword() {
		return user.getEncodedPassword();
	}

	@Override
	public String getUsername() {
		return user.getPhoneNumber();
	}
}
