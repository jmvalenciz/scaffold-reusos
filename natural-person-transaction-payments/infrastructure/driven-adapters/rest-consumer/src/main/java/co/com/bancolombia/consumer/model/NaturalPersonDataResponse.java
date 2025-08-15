package co.com.bancolombia.consumer.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NaturalPersonDataResponse {
    private Meta meta;
    private NaturalPersonDataDTO data;
}
