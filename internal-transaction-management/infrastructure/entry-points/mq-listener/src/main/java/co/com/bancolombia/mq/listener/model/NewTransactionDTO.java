package co.com.bancolombia.mq.listener.model;

import lombok.Data;
import lombok.Getter;
import org.beanio.annotation.Field;
import org.beanio.annotation.Record;
import org.beanio.builder.Align;

@Getter
@Record
@Data
public class NewTransactionDTO {
    @Field(length = 36)
    String origin;
    @Field(length = 36)
    String destination;
    @Field(length = 3)
    String currency;
    @Field(length = 3)
    String consumerAcronym;
    @Field(length = 12, padding = '0', align = Align.RIGHT)
    long amount;
}
