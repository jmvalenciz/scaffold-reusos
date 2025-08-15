package co.com.bancolombia.api;

import co.com.bancolombia.api.middleware.GetTransactionsMiddleware;
import co.com.bancolombia.api.middleware.NewTransactionMiddleware;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.HEAD;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@AllArgsConstructor
public class RouterRest {
    private final NewTransactionMiddleware newTransactionMiddleware;
    private final GetTransactionsMiddleware getTransactionsMiddleware;

    @Bean
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(HEAD("/health"), handler::listenHealthcheck)
                .and(route(POST("/create-transaction"), handler::listenCreateTransaction)
                        .filter(newTransactionMiddleware))
                .and(route(GET("/transactions"), handler::listenGetTransactionsByAccount)
                        .filter(getTransactionsMiddleware));
    }
}
