package co.com.bancolombia.api;

import co.com.bancolombia.api.exception.RequestException;
import co.com.bancolombia.api.model.*;
import co.com.bancolombia.model.account.AccountId;
import co.com.bancolombia.usecase.createnewtransaction.CreateNewTransactionUseCase;
import co.com.bancolombia.model.exceptions.BusinessException;
import co.com.bancolombia.usecase.gettransactionsbyaccount.GetTransactionsByAccountUseCase;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class Handler {
    private final CreateNewTransactionUseCase createNewTransactionUseCase;
    private final GetTransactionsByAccountUseCase getTransactionsByAccountUseCase;

    public Mono<ServerResponse> listenCreateTransaction(ServerRequest serverRequest) {
        NewTransactionDTO body = (NewTransactionDTO) serverRequest.attribute("body").get();
        NewTransactionHeaders headers = (NewTransactionHeaders) serverRequest.attribute("headers").get();

        return createNewTransactionUseCase.execute(body.toModel(), headers.getConsumerAcronym(), headers.getMessageId())
                .flatMap(transaction -> ServerResponse.ok()
                        .bodyValue(ResponseWrapper.builder()
                                .data(transaction)
                                .meta(Meta.builder()
                                        .requestDateTime(LocalDateTime.now())
                                        .applicationId(Constants.APPLICATION_NAME)
                                        .messageId(headers.getMessageId())
                                        .build())
                                .build()))
                .onErrorResume(this::errorHandler);
    }

    public Mono<ServerResponse> listenGetTransactionsByAccount(ServerRequest serverRequest) {
        GetTransactionsHeaders headers = (GetTransactionsHeaders) serverRequest.attribute("headers").get();
        var body = (GetTransactionsDTO) serverRequest.attribute("body").get();
        return getTransactionsByAccountUseCase.execute(body.toModel(), headers.getMessageId())
                .flatMap(transactions -> ServerResponse.ok()
                        .bodyValue(ResponseWrapper.builder()
                                .meta(Meta.builder()
                                        .requestDateTime(LocalDateTime.now())
                                        .applicationId(Constants.APPLICATION_NAME)
                                        .messageId(headers.getMessageId())
                                        .build())
                                .data(transactions)
                                .build()))
                .onErrorResume(this::errorHandler);
    }

    public Mono<ServerResponse> listenHealthcheck(ServerRequest serverRequest) {
        return ServerResponse.ok().build();
    }

    @SneakyThrows
    private Mono<ServerResponse> errorHandler(Throwable e){
        return switch (e){
            case BusinessException be -> ServerResponse.status(400).bodyValue(be.getMessage()+": "+be.getData());
            case RequestException re -> ServerResponse.status(re.getErrorMessage().getStatusCode()).bodyValue(re.getMessage());
            default -> throw e;
        };
    }
}
