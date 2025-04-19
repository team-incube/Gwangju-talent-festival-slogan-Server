package team.incube.gwangjutalentfestivalserver.domain.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {
	@Email
	private String email;

	@Pattern(
			regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[\\W_]).{8,}$",
			message = "특수문자, 영문과 숫자를 포함한 8자리 이상의 비밀번호를 만들어주세요."
	)
	private String password;
}