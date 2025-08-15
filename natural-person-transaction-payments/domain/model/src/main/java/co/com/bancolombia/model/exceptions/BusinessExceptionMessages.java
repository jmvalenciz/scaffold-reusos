package co.com.bancolombia.model.exceptions;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum BusinessExceptionMessages {
    ACCOUNT_NOT_FOUND("BE001", "Account not found"),
    NOT_ENOUGH_FOUNDS("BE002", "Origin account doesn't have enough founds"),
    NATURAL_PERSON_NO_ACTIVE("BE003", "Natural person is not active"),
    UNHANDLED_EXCEPTION("BE004", "Unhandled exception");

    private final String code;
    private final String message;

}
