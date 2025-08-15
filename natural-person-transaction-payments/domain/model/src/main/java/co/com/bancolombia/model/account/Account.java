package co.com.bancolombia.model.account;
import co.com.bancolombia.model.naturalpersondata.NaturalPersonId;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
//import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
//@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Account {
    private final String id;
    private final NaturalPersonId owner;
    private final String type;
    private final String currency;
    private final long number;
}
