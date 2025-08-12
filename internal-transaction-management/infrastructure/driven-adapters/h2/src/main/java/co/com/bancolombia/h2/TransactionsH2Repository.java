package co.com.bancolombia.h2;

import co.com.bancolombia.h2.model.TransactionEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface TransactionsH2Repository extends ReactiveCrudRepository<TransactionEntity, UUID> {
    Flux<TransactionEntity> findByOriginAccountOrDestinationAccount(String origin, String destination);
}
