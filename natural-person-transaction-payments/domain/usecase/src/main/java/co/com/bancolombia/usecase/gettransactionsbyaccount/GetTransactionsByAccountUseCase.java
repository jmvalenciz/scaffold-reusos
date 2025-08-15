package co.com.bancolombia.usecase.gettransactionsbyaccount;

import co.com.bancolombia.model.account.AccountId;
import co.com.bancolombia.model.transaction.Transaction;
import co.com.bancolombia.model.transaction.gateways.TransactionCacheRepository;
import co.com.bancolombia.model.transaction.gateways.TransactionService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public class GetTransactionsByAccountUseCase {
    private final TransactionService transactionService;
    private final TransactionCacheRepository cache;

    public Mono<List<Transaction>> execute(AccountId accountId, String messageId){
        return cache.getTransactionsCacheByAccount(accountId.getId())
                .switchIfEmpty(Mono.defer(()-> transactionService.getTransactionsByAccount(accountId.getId())
                        .flatMap(transactions -> cache.saveTransactionsCache(transactions, accountId.getId()))
                ));
        //return cache.getTransactionsCacheByAccount(accountId.getId())
        //        .switchIfEmpty(Mono.defer(()-> transactionService.getTransactionsByAccount(accountId.getId())
        //            .flatMap(transactions -> cache.saveTransactionsCache(transactions, accountId.getId()))
        //        ));
    }
}
