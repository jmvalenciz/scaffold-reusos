package co.com.bancolombia.model.naturalpersondata;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder(toBuilder = true)
public class NaturalPersonData {
    NaturalPersonId id;
    String fullName;
    String status;
    List<String> accounts;
}
