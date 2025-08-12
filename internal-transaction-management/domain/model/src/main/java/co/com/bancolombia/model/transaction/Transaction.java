package co.com.bancolombia.model.transaction;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder(toBuilder = true)
public class Transaction {
    String id;
    String origin;
    String destination;
    String currency;
    long amount;
    LocalDateTime createdAt;
}
