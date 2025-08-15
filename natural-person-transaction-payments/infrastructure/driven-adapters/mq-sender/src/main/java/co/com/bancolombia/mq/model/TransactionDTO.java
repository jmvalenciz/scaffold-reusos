package co.com.bancolombia.mq.model;

import co.com.bancolombia.model.transaction.Transaction;
import lombok.*;
import org.beanio.annotation.Field;
import org.beanio.annotation.Record;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Data
@Record(minOccurs = 0)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {
    @Field(length = 1, rid = true, literal = "D")
    private String recordType = "D";
    @Field(length = 36)
    String id;
    @Field(length = 36)
    String origin;
    @Field(length = 36)
    String destination;
    @Field(length = 3)
    String currency;
    @Field(length = 26)
    LocalDateTime createdAt;
    @Field(length = 12)
    long amount;

    public Transaction toModel(){
        return Transaction.builder()
                .id(id)
                .amount(amount)
                .createdAt(Date.from(createdAt.atZone(ZoneId.systemDefault()).toInstant()))
                .currency(currency)
                .destination(destination)
                .origin(origin)
                .build();
    }
}
