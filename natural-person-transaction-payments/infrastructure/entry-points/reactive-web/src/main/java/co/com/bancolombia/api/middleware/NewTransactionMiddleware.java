package co.com.bancolombia.api.middleware;

import co.com.bancolombia.api.model.NewTransactionDTO;
import co.com.bancolombia.api.model.NewTransactionHeaders;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class NewTransactionMiddleware implements HandlerFilterFunction<ServerResponse, ServerResponse> {
    static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    @Override
    public Mono<ServerResponse> filter(ServerRequest request, HandlerFunction<ServerResponse> next) {
        var headers = request.headers();
        NewTransactionHeaders newTransactionHeaders = NewTransactionHeaders.builder()
                .messageId(headers.firstHeader("message-id"))
                .contentType(headers.firstHeader(HttpHeaders.CONTENT_TYPE))
                .consumerAcronym(headers.firstHeader("consumer-acronym"))
                .build();
        var headersViolations = validator.validate(newTransactionHeaders);
        if(!headersViolations.isEmpty()){
            return ServerResponse.status(400).bodyValue(headersViolations.stream()
                    .map(v->v.getPropertyPath().toString()+": "+v.getMessage())
                    .toList());
        }
        return request.bodyToMono(NewTransactionDTO.class).flatMap(body->{
            var bodyViolations = validator.validate(body);
            if(!bodyViolations.isEmpty()){
                return ServerResponse.status(400).bodyValue(bodyViolations.stream()
                        .map(v->v.getPropertyPath()+": "+v.getMessage())
                        .toList());
            }
            return next.handle(ServerRequest.from(request)
                    .attribute("headers", newTransactionHeaders)
                    .attribute("body", body)
                    .build());
        });
    }
}
