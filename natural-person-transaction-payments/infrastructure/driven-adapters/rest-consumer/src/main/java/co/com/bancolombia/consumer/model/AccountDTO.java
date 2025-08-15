package co.com.bancolombia.consumer.model;

import co.com.bancolombia.model.account.Account;
import co.com.bancolombia.model.naturalpersondata.NaturalPersonId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {
    private String id;
    private String type;
    private NaturalPersonId owner;
    private String currency;
    private long number;

    public Account toModel(){
        return Account.builder()
                .id(id)
                .number(number)
                .owner(owner)
                .currency(currency)
                .type(type)
                .build();
    }
}
