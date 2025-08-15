package co.com.bancolombia.mq.listener.model;

import co.com.bancolombia.model.transaction.Transaction;
import lombok.Builder;
import lombok.Data;
import org.beanio.annotation.Field;
import org.beanio.annotation.Record;

import java.time.LocalDateTime;

@Data
@Record(minOccurs = 0)
@Builder
public class TransactionResponseDTO {
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

    public static TransactionResponseDTO fromModel(Transaction transaction){
        return TransactionResponseDTO.builder()
                .id(transaction.getId())
                .recordType("D")
                .createdAt(transaction.getCreatedAt())
                .destination(transaction.getDestination())
                .origin(transaction.getOrigin())
                .amount(transaction.getAmount())
                .currency(transaction.getCurrency())
                .build();
    }
}
