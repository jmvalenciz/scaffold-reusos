package co.com.bancolombia.consumer.model;

import co.com.bancolombia.model.naturalpersondata.NaturalPersonData;
import co.com.bancolombia.model.naturalpersondata.NaturalPersonId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NaturalPersonDataDTO {
    private NaturalPersonId document;
    private String fullName;
    private String status;
    private List<String> accounts;

    public NaturalPersonData toModel(){
        return NaturalPersonData.builder()
                .id(document)
                .accounts(accounts)
                .fullName(fullName)
                .status(status)
                .build();
    }
}
