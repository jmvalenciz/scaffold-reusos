package co.com.bancolombia.model.transaction;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class NewTransaction{
    String origin;
    String destination;
    long amount;
    String currency;
    String consumerAcronym;
}
