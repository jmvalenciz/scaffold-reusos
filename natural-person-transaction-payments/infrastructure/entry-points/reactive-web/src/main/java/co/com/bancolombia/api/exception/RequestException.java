package co.com.bancolombia.api.exception;

import lombok.Getter;

@Getter
public class RequestException extends RuntimeException {
    private final RequestExceptionMessage errorMessage;
    public RequestException(RequestExceptionMessage message) {
        super(message.getCode()+": "+message.getMessage());
        this.errorMessage = message;
    }
}
