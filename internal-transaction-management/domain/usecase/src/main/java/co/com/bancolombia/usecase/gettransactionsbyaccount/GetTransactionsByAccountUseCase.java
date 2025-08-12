package co.com.bancolombia.usecase.gettransactionsbyaccount;

import co.com.bancolombia.model.transaction.Transaction;
import co.com.bancolombia.model.transaction.gateways.TransactionRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
public class GetTransactionsByAccountUseCase {
    private final TransactionRepository repository;
    public Flux<Transaction> getTransactions(String accountId){
        return repository.getTransactionsByAccount(accountId);
    }
}
