package co.com.bancolombia.model.transaction;
import lombok.*;
//import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Transaction {
    String id;
    String origin;
    String destination;
    String currency;
    Date createdAt;
    long amount;
}
