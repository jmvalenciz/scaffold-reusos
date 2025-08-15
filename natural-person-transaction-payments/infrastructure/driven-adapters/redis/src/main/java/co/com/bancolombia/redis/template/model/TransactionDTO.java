package co.com.bancolombia.redis.template.model;

import lombok.Data;
import java.util.Date;

@Data
public class TransactionDTO {
    String id;
    String origin;
    String destination;
    String currency;
    Date createdAt;
    long amount;
}
