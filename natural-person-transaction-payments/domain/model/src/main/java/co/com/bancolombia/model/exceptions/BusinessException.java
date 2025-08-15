package co.com.bancolombia.model.exceptions;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final String data;
    public BusinessException(BusinessExceptionMessages message, String data) {
        super(message.getCode()+": "+message.getMessage());
        this.data = data;
    }
}
