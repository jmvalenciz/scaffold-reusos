package co.com.bancolombia.model.account.gateways;

import co.com.bancolombia.model.account.Account;
import co.com.bancolombia.model.account.AccountId;
import reactor.core.publisher.Mono;

public interface AccountService {
    Mono<Account> getAccount(AccountId accountId, String messageId);
    Mono<Account> updateAccountBalance(AccountId accountId, long amount, String messageId);
}
