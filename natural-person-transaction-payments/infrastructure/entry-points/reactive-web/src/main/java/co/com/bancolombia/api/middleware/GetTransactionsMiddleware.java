package co.com.bancolombia.api.middleware;

import co.com.bancolombia.api.Constants;
import co.com.bancolombia.api.model.GetTransactionsDTO;
import co.com.bancolombia.api.model.GetTransactionsHeaders;
import jakarta.validation.ConstraintViolation;
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
public class GetTransactionsMiddleware implements HandlerFilterFunction<ServerResponse, ServerResponse> {
    static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Override
    public Mono<ServerResponse> filter(ServerRequest request, HandlerFunction<ServerResponse> next) {
        var headers = request.headers();
        var getTransactionsHeaders = GetTransactionsHeaders.builder()
                .messageId(headers.firstHeader("message-id"))
                .consumerAcronym(headers.firstHeader("consumer-acronym"))
                .contentType(headers.firstHeader(HttpHeaders.CONTENT_TYPE))
                .build();
        var headersViolations = validator.validate(getTransactionsHeaders);
        if(!headersViolations.isEmpty()){
            return ServerResponse.status(400).bodyValue(headersViolations.stream()
                    .map(v->v.getPropertyPath().toString()+": "+v.getMessage())
                    .toList());
        }
        return request.bodyToMono(GetTransactionsDTO.class)
                .flatMap(body ->{
                    var bodyViolations = validator.validate(body);
                    if(!bodyViolations.isEmpty()){
                        return ServerResponse.status(400).bodyValue(bodyViolations.stream()
                                .map(ConstraintViolation::getMessage)
                                .toList());
                    }
                    return next.handle(ServerRequest.from(request)
                            .attribute("headers", getTransactionsHeaders)
                            .attribute("body", body)
                            .build());
                });
    }
}
