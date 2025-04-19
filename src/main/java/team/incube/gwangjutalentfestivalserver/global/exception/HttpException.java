package team.incube.gwangjutalentfestivalserver.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class HttpException extends RuntimeException {
	private HttpStatus status;
	private String message;
}