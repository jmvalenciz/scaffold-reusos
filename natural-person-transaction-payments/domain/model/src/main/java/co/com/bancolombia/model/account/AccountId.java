package co.com.bancolombia.model.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class AccountId {
    private final String id;
    private final String type;
}
