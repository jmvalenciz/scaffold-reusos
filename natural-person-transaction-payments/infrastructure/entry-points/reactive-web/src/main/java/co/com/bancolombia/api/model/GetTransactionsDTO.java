package co.com.bancolombia.api.model;

import co.com.bancolombia.api.Constants;
import co.com.bancolombia.model.account.AccountId;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class GetTransactionsDTO {
    @NotNull(message = "account-id is required")
    @Valid
    @Pattern(regexp = Constants.UUID_PATTERN, message = "must be UUID")
            @JsonProperty("account-id")
    String originAccountId;

    @NotNull(message = "account-type is required")
    @Valid
    @Pattern(regexp = Constants.ACCOUNT_TYPE_PATTERN)
    @JsonProperty("account-type")
    String originAccountType;

    public AccountId toModel(){
        return AccountId.builder()
                .id(originAccountId)
                .type(originAccountType)
                .build();
    }
}
