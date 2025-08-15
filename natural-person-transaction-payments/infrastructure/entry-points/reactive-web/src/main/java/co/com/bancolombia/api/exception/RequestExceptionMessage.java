package co.com.bancolombia.api.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum RequestExceptionMessage {
    NOT_AUTHORIZED("RE001", "Not authorized", HttpStatus.UNAUTHORIZED),
    BAD_REQUEST_HEADERS("RE003", "Bad request headers", HttpStatus.BAD_REQUEST),
    BAD_REQUEST_BODY("RE002", "Bad request body", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus statusCode;
}
