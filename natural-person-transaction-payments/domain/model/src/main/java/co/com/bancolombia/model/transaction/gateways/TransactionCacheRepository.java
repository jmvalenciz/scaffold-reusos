package co.com.bancolombia.model.transaction.gateways;

import co.com.bancolombia.model.transaction.Transaction;
import reactor.core.publisher.Mono;

import java.util.List;

public interface TransactionCacheRepository {
    Mono<List<Transaction>> saveTransactionsCache(List<Transaction> transactions, String accountId);
    Mono<List<Transaction>> getTransactionsCacheByAccount(String accountId);
}
