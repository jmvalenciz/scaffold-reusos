package co.com.bancolombia.model.transaction.gateways;

import co.com.bancolombia.model.transaction.NewTransaction;
import co.com.bancolombia.model.transaction.Transaction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TransactionRepository {
    Flux<Transaction> getTransactionsByAccount(String accountId);
    Mono<Transaction> createTransaction(NewTransaction newTransaction);
}
