package co.com.bancolombia.mq.model;

import lombok.Builder;
import org.beanio.annotation.Field;
import org.beanio.annotation.Record;

@Record
@Builder
public class GetTransactionsRequest {
    @Field(length = 15)
    String operation;
    @Field(length = 36)
    String accountId;
}
