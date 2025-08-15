package co.com.bancolombia.mq.model;

import lombok.Builder;
import org.beanio.annotation.Field;
import org.beanio.annotation.Record;
import org.beanio.annotation.Segment;

@Record
@Builder
public class NewTransactionRequest {
    @Field(length = 15)
    String operation;
    @Segment()
    NewTransactionDTO newTransactionDTO;
}
