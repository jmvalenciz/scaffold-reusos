package co.com.bancolombia.model.transaction.gateways;

import co.com.bancolombia.model.transaction.NewTransaction;
import co.com.bancolombia.model.transaction.Transaction;
import reactor.core.publisher.Mono;

import java.util.List;

public interface TransactionService {
    Mono<Transaction> createTransaction(NewTransaction newTransaction, String consumerAcronym);
    Mono<List<Transaction>> getTransactionsByAccount(String accountId);
}
