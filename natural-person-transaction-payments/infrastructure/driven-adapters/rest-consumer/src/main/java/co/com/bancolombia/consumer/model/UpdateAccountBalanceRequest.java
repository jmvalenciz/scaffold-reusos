package co.com.bancolombia.consumer.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UpdateAccountBalanceRequest {
    String id;
    String type;
    long amount;
}
