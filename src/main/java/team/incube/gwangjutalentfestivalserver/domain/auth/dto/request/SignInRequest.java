package team.incube.gwangjutalentfestivalserver.domain.auth.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignInRequest {
	@NotNull
	private String email;

	@NotNull
	private String password;
}