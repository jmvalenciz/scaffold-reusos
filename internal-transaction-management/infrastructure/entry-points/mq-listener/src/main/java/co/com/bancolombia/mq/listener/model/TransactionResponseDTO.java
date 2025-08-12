package co.com.bancolombia.mq.listener.model;

import lombok.Builder;
import lombok.Data;
import org.beanio.annotation.Field;
import org.beanio.annotation.Record;

import java.time.LocalDateTime;

@Data
@Record(minOccurs = 0)
@Builder
public class TransactionResponseDTO {
    @Field(at = 0, length = 1, rid = true, literal = "D")
    private String recordType = "D";
    @Field(at = 1, length = 36)
    String id;
    @Field(at = 1, length = 36)
    String origin;
    @Field(at = 1, length = 36)
    String destination;
    @Field(at = 1, length = 3)
    String currency;
    @Field(at = 1)
    LocalDateTime createdAt;
    @Field(at = 1)
    long amount;
}
