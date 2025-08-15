package co.com.bancolombia.consumer;

import co.com.bancolombia.consumer.config.JWTGenerator;
import co.com.bancolombia.consumer.model.AccountResponse;
import co.com.bancolombia.consumer.model.UpdateAccountBalanceRequest;
import co.com.bancolombia.model.account.Account;
import co.com.bancolombia.model.account.AccountId;
import co.com.bancolombia.model.account.gateways.AccountService;
import co.com.bancolombia.model.exceptions.BusinessException;
import co.com.bancolombia.model.exceptions.BusinessExceptionMessages;
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
public class NaturalPersonAccountRestConsumer implements AccountService {
    private final WebClient client;
    private final JWTGenerator jwtGenerator;

    @Autowired
    public NaturalPersonAccountRestConsumer(@Qualifier("naturalPersonAccount") WebClient client, JWTGenerator jwtGenerator){
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
    @CircuitBreaker(name = "getAccount" /*, fallbackMethod = "testGetOk"*/)
    public Mono<Account> getAccount(AccountId accountId, String messageId) {
        String jwt = jwtGenerator.createJwtForSubject("request-natural-person-account");
        return client.post()
                .uri("/retreive-customer-account")
                .header(HttpHeaders.AUTHORIZATION, "Bearer "+jwt)
                .header("message-id", messageId)
                .body(Mono.just(accountId), AccountId.class)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, this::handleRequestException)
                .onStatus(HttpStatusCode::is5xxServerError, this::handleServerException)
                .bodyToMono(AccountResponse.class)
                .map(accountResponse -> accountResponse.getData().toModel());
    }

    @Override
    @CircuitBreaker(name = "updateAccountBalance" /*, fallbackMethod = "testGetOk"*/)
    public Mono<Account> updateAccountBalance(AccountId accountId, long amount, String messageId) {
        String jwt = jwtGenerator.createJwtForSubject("request-natural-person-account");
        var body = UpdateAccountBalanceRequest.builder()
                .id(accountId.getId())
                .type(accountId.getType())
                .amount(amount)
                .build();
        return client.post()
                .uri("/update-customer-account-balance")
                .header(HttpHeaders.AUTHORIZATION, "Bearer "+jwt)
                .header("message-id", messageId)
                .body(Mono.just(body), UpdateAccountBalanceRequest.class)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, this::handleRequestException)
                .onStatus(HttpStatusCode::is5xxServerError, this::handleServerException)
                .bodyToMono(AccountResponse.class)
                .map(accountResponse -> accountResponse.getData().toModel());
    }

    private Mono<?extends RuntimeException> handleServerException(ClientResponse clientResponse) {
        return Mono.error(new RuntimeException());
    }

    private Mono<?extends RuntimeException> handleRequestException(ClientResponse clientResponse) {
        return clientResponse.bodyToMono(String.class)
                .flatMap(body->Mono.error(new BusinessException(BusinessExceptionMessages.UNHANDLED_EXCEPTION, body)));
    }
}
