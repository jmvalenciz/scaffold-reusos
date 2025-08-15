package co.com.bancolombia.api.model;

import co.com.bancolombia.api.Constants;
import co.com.bancolombia.model.account.AccountId;
import co.com.bancolombia.model.transaction.NewTransaction;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.Map;

@Data
public class NewTransactionDTO {
    @NotNull(message = "is required")
    @Valid
    @Pattern(regexp = Constants.UUID_PATTERN, message = "must be UUID")
    String originAccountId;

    @NotNull(message = "is required")
    @Valid
    @Pattern(regexp = "^(CORRIENTE|AHORROS)$")
    String originAccountType;

    @NotNull(message = "is required")
    @Valid
    @Pattern(regexp = Constants.UUID_PATTERN, message = "must be UUID")
    String destinationAccountId;

    @NotNull(message = "is required")
    @Valid
    @Pattern(regexp = "^(CORRIENTE|AHORROS)$")
    String destinationAccountType;

    @NotNull(message = "is required")
    @Valid
    @Min(1000)
    long amount;

    @NotNull(message = "is required")
    @Valid
    @Pattern(regexp = "^(COP)$")
    String currency;

    @JsonProperty("origin")
    private void unpackOrigin(Map<String, Object> origin){
        this.originAccountId = (String) origin.get("account_id");
        this.originAccountType = (String) origin.get("account_type");
    }

    @JsonProperty("destination")
    private void unpackDestination(Map<String, Object> origin){
        this.destinationAccountId = (String) origin.get("account_id");
        this.destinationAccountType = (String) origin.get("account_type");
    }

    public NewTransaction toModel(){
        return NewTransaction.builder()
                .amount(this.amount)
                .origin(new AccountId(this.originAccountId, this.originAccountType))
                .destination(new AccountId(this.destinationAccountId, this.destinationAccountType))
                .currency(currency)
                .build();
    }
}
