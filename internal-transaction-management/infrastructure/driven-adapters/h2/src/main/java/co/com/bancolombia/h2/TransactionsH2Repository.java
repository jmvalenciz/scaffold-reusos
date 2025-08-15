package co.com.bancolombia.h2;

import co.com.bancolombia.h2.model.TransactionEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface TransactionsH2Repository extends ReactiveCrudRepository<TransactionEntity, UUID> {
    @Query("SELECT * FROM transactions WHERE ORIGIN_ACCOUNT = ?1 OR DESTINATION_ACCOUNT = ?2")
    Flux<TransactionEntity> findByOriginAccountOrDestinationAccount(String originAccount, String destinationAccount);
}
