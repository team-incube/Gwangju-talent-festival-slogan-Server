package team.incube.gwangjutalentfestivalserver.global.exception.handler;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import team.incube.gwangjutalentfestivalserver.global.exception.HttpException;
import team.incube.gwangjutalentfestivalserver.global.exception.dto.HttpExceptionResponse;

@RestControllerAdvice
public class HttpExceptionHandler {

	@ExceptionHandler(HttpException.class)
	public ResponseEntity<HttpExceptionResponse> httpException(HttpException exception) {
		HttpExceptionResponse response = HttpExceptionResponse.builder()
				.status(exception.getStatus())
				.message(exception.getMessage())
				.build();
		return ResponseEntity.status(response.getStatus()).body(response);
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<HttpExceptionResponse> runtimeException(RuntimeException exception) {
		HttpExceptionResponse response = new HttpExceptionResponse(
			HttpStatus.INTERNAL_SERVER_ERROR,
			exception.getMessage() != null ? exception.getMessage() : ""
		);
		return ResponseEntity.status(response.getStatus()).body(response);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<HttpExceptionResponse> methodArgumentNotValidException(
		MethodArgumentNotValidException exception
	) {
		String reason = exception.getBindingResult()
			.getFieldErrors()
			.stream()
			.findFirst()
			.map(DefaultMessageSourceResolvable::getDefaultMessage)
			.orElse("잘못된 요청입니다.");

        HttpExceptionResponse response = new HttpExceptionResponse(
			HttpStatus.BAD_REQUEST,
			reason
		);
		return ResponseEntity.status(response.getStatus()).body(response);
	}
}
