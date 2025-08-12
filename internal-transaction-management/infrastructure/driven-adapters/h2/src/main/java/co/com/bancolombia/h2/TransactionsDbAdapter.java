package co.com.bancolombia.h2;

import co.com.bancolombia.h2.model.TransactionEntity;
import co.com.bancolombia.model.transaction.NewTransaction;
import co.com.bancolombia.model.transaction.Transaction;
import co.com.bancolombia.model.transaction.gateways.TransactionRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public class TransactionsDbAdapter implements TransactionRepository {
    private final TransactionsH2Repository repository;

    TransactionsDbAdapter(TransactionsH2Repository repository){
        this.repository = repository;
    }

    @Override
    public Flux<Transaction> getTransactionsByAccount(String accountId) {
        return repository.findByOriginAccountOrDestinationAccount(accountId, accountId)
                .map(this::entityToModel);
    }

    @Override
    public Mono<Transaction> createTransaction(NewTransaction newTransaction) {
        var entity = modelToEntity(newTransaction);
        return repository.save(entity)
                .map(this::entityToModel);
    }

    private Transaction entityToModel(TransactionEntity transaction){
        return Transaction.builder()
                .id(transaction.getId().toString())
                .amount(transaction.getAmount())
                .createdAt(transaction.getCreatedAt())
                .origin(transaction.getOriginAccount())
                .destination(transaction.getDestinationAccount())
                .currency(transaction.getCurrency())
                .build();
    }

    private TransactionEntity modelToEntity(NewTransaction transaction){
        return TransactionEntity.builder()
                .id(UUID.randomUUID())
                .amount(transaction.getAmount())
                .createdAt(LocalDateTime.now())
                .originAccount(transaction.getOrigin())
                .destinationAccount(transaction.getDestination())
                .currency(transaction.getCurrency())
                .build();
    }

}
