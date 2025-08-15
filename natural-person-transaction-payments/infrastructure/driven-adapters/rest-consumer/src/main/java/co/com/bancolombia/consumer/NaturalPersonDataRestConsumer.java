package co.com.bancolombia.consumer;

import co.com.bancolombia.consumer.config.JWTGenerator;
import co.com.bancolombia.consumer.model.NaturalPersonDataResponse;
import co.com.bancolombia.model.exceptions.BusinessException;
import co.com.bancolombia.model.exceptions.BusinessExceptionMessages;
import co.com.bancolombia.model.naturalpersondata.NaturalPersonData;
import co.com.bancolombia.model.naturalpersondata.NaturalPersonId;
import co.com.bancolombia.model.naturalpersondata.gateways.NaturalPersonDataService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class NaturalPersonDataRestConsumer implements NaturalPersonDataService {
    private final WebClient client;
    private final JWTGenerator jwtGenerator;

    @Autowired
    public NaturalPersonDataRestConsumer(@Qualifier("naturalPersonData") WebClient client, JWTGenerator jwtGenerator){
        this.client = client;
        this.jwtGenerator = jwtGenerator;
    }

// Possible fallback method
//    public Mono<String> testGetOk(Exception ignored) {
//        return client
//                .get() // TODO: change for another endpoint or destination
//                .retrieve()
//                .bodyToMono(String.class);
//    }

    @Override
    @CircuitBreaker(name = "getNaturalPersonData" /*, fallbackMethod = "testGetOk"*/)
    public Mono<NaturalPersonData> getNaturalPersonData(NaturalPersonId owner, String messageId) {
        var jwt = jwtGenerator.createJwtForSubject("request-natural-person-data");
        return client.post()
                .uri("/retreive-customer-data")
                .body(Mono.just(owner), NaturalPersonId.class)
                .header("message-id", messageId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer "+jwt)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, this::handleRequestException)
                .onStatus(HttpStatusCode::is5xxServerError, this::handleServerException)
                .bodyToMono(NaturalPersonDataResponse.class)
                .map(response-> response.getData().toModel());
    }

    private Mono<?extends RuntimeException> handleServerException(ClientResponse clientResponse) {
        return Mono.error(new RuntimeException());
    }

    private Mono<?extends RuntimeException> handleRequestException(ClientResponse clientResponse) {
        return clientResponse.bodyToMono(String.class)
                .flatMap(body -> Mono.error(new BusinessException(BusinessExceptionMessages.UNHANDLED_EXCEPTION, body)) );
    }

}
