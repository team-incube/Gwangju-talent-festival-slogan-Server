package team.incube.gwangjutalentfestivalserver.global.exception.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
@AllArgsConstructor
public class HttpExceptionResponse {
    private HttpStatus status;
    private String message;
}
