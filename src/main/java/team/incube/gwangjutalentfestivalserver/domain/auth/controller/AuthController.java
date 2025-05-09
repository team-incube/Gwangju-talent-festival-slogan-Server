package team.incube.gwangjutalentfestivalserver.domain.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.incube.gwangjutalentfestivalserver.domain.auth.dto.AdminLoginRequest;
import team.incube.gwangjutalentfestivalserver.domain.auth.dto.AdminLoginResponse;
import team.incube.gwangjutalentfestivalserver.domain.auth.usecase.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
	private final LoginAdminUsecase loginAdminUsecase;

	@PostMapping("/login")
	public ResponseEntity<AdminLoginResponse> login(
		@Valid @RequestBody AdminLoginRequest adminLoginRequest
	) {
		return ResponseEntity.ok(
			loginAdminUsecase.execute(adminLoginRequest)
		);
	}
}
