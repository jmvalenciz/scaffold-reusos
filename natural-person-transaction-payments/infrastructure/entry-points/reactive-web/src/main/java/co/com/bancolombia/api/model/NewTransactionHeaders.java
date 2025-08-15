package co.com.bancolombia.api.model;

import co.com.bancolombia.api.Constants;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@Builder
public class NewTransactionHeaders {
    @Valid
    @NotNull
    @Pattern(regexp = Constants.UUID_PATTERN, message = "Must be an UUID")
    String messageId;

    @Pattern(regexp = Constants.CONSUMER_ACRONYM_PATTERN)
    @Valid
    @NotNull
    String consumerAcronym;

    @Valid
    @NotNull
    @Pattern(regexp = "^(application/json)$")
    String contentType;
}
