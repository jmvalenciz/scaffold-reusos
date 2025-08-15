package co.com.bancolombia.h2;

import co.com.bancolombia.h2.model.TransactionEntity;
import co.com.bancolombia.model.transaction.NewTransaction;
import co.com.bancolombia.model.transaction.Transaction;
import co.com.bancolombia.model.transaction.gateways.TransactionRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class TransactionsDbAdapter implements TransactionRepository {
    private final List<Transaction> repository = new ArrayList<>();

    @Override
    public Flux<Transaction> getTransactionsByAccount(String accountId) {
        var transactions = repository.stream()
                .filter(t -> t.getOrigin().equals(accountId)||t.getDestination().equals(accountId));
        return Flux.fromStream(transactions);
    }

    @Override
    public Mono<Transaction> createTransaction(NewTransaction newTransaction) {
        var transaction = Transaction.builder()
                .id(UUID.randomUUID().toString())
                .amount(newTransaction.getAmount())
                .createdAt(LocalDateTime.now())
                .origin(newTransaction.getOrigin())
                .destination(newTransaction.getDestination())
                .currency(newTransaction.getCurrency())
                .build();
        repository.add(transaction);
        return Mono.just(transaction);
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
                //.id(UUID.randomUUID())
                .amount(transaction.getAmount())
                .createdAt(LocalDateTime.now())
                .originAccount(transaction.getOrigin())
                .destinationAccount(transaction.getDestination())
                .currency(transaction.getCurrency())
                .build();
    }

}
