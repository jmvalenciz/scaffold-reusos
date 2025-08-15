package co.com.bancolombia.usecase.createnewtransaction;

import co.com.bancolombia.model.transaction.NewTransaction;
import co.com.bancolombia.model.transaction.Transaction;
import co.com.bancolombia.model.transaction.gateways.TransactionRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CreateNewTransactionUseCase {
    private final TransactionRepository transactionRepository;

    public Mono<Transaction> newTransaction(NewTransaction newTransaction){
        return transactionRepository.createTransaction(newTransaction);
    }
}
