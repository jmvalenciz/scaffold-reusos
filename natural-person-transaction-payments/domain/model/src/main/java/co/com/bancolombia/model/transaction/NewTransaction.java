package co.com.bancolombia.model.transaction;

import co.com.bancolombia.model.account.AccountId;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NewTransaction {
    AccountId origin;
    AccountId destination;
    String currency;
    long amount;
}
