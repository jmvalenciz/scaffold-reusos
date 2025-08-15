package co.com.bancolombia.model.naturalpersondata.gateways;

import co.com.bancolombia.model.naturalpersondata.NaturalPersonData;
import co.com.bancolombia.model.naturalpersondata.NaturalPersonId;
import reactor.core.publisher.Mono;

public interface NaturalPersonDataService {
    Mono<NaturalPersonData> getNaturalPersonData(NaturalPersonId owner, String messageId);
}
