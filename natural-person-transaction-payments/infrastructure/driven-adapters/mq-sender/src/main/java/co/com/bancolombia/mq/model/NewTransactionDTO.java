package co.com.bancolombia.mq.model;

import co.com.bancolombia.model.transaction.NewTransaction;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.beanio.annotation.Field;
import org.beanio.annotation.Record;
import org.beanio.builder.Align;

@Getter
@Record
@Data
@Builder
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

    public static NewTransactionDTO fromModel(NewTransaction newTransaction, String consumerAcronym){
        return NewTransactionDTO.builder()
                .amount(newTransaction.getAmount())
                .consumerAcronym(consumerAcronym)
                .currency(newTransaction.getCurrency())
                .origin(newTransaction.getOrigin().getId())
                .destination(newTransaction.getDestination().getId())
                .build();
    }
}