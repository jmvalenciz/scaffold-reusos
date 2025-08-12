package co.com.bancolombia.h2.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Table("transactions")
@Data
@Getter
@Builder(toBuilder = true)
public class TransactionEntity {
    @Id
    UUID id;
    String originAccount;
    String destinationAccount;
    String currency;
    long amount;
    LocalDateTime createdAt;
}
