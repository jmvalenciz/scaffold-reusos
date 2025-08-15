package co.com.bancolombia.mq.listener.model;

import lombok.Builder;
import lombok.Getter;
import org.beanio.annotation.Field;
import org.beanio.annotation.Record;

import java.util.List;

@Record(minOccurs = 1, maxOccurs = 1)
@Builder
@Getter
public class TransactionListDTO {
    @Field(minOccurs = 0, maxOccurs = -1, collection = List.class)
    List<TransactionResponseDTO> transactions;
}
