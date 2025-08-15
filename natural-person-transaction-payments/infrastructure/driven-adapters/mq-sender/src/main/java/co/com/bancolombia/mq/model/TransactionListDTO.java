package co.com.bancolombia.mq.model;

import co.com.bancolombia.model.transaction.Transaction;
import lombok.Builder;
import org.beanio.annotation.Field;
import org.beanio.annotation.Record;

import java.util.List;

@Record(minOccurs = 1, maxOccurs = 1)
@Builder
public class TransactionListDTO {
    @Field(minOccurs = 2, maxOccurs = -2, collection = List.class)
    List<TransactionDTO> transactions;

    public List<Transaction> toModel(){
        return transactions.stream().map(TransactionDTO::toModel).toList();
    }
}