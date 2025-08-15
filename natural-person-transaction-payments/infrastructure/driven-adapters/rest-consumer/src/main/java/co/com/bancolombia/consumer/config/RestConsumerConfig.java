package co.com.bancolombia.consumer.config;

import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import static io.netty.channel.ChannelOption.CONNECT_TIMEOUT_MILLIS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Configuration
public class RestConsumerConfig {

    private final String naturalPersonDataUrl;
    private final String naturalPersonAccountUrl;

    private final int timeout;

    public RestConsumerConfig(@Value("${adapter.restconsumer.natural-person-data-url}") String naturalPersonDataUrl,
                              @Value("${adapter.restconsumer.natural-person-account-url}") String naturalPersonAccountUrl,
                              @Value("${adapter.restconsumer.timeout}") int timeout) {
        this.naturalPersonDataUrl = naturalPersonDataUrl;
        this.naturalPersonAccountUrl = naturalPersonAccountUrl;
        this.timeout = timeout;
    }

    @Bean("naturalPersonAccount")
    public WebClient getNaturalPersonAccountWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl(naturalPersonAccountUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .clientConnector(getClientHttpConnector())
                .build();
    }

    @Bean("naturalPersonData")
    public WebClient getNaturalPersnDataWebClient(WebClient.Builder builder) {
        return builder
            .baseUrl(naturalPersonDataUrl)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
            .clientConnector(getClientHttpConnector())
            .build();
    }

    private ClientHttpConnector getClientHttpConnector() {
        /*
        IF YO REQUIRE APPEND SSL CERTIFICATE SELF SIGNED: this should be in the default cacerts trustore
        */
        return new ReactorClientHttpConnector(HttpClient.create()
                .compress(true)
                .keepAlive(true)
                .option(CONNECT_TIMEOUT_MILLIS, timeout)
                .doOnConnected(connection -> {
                    connection.addHandlerLast(new ReadTimeoutHandler(timeout, MILLISECONDS));
                    connection.addHandlerLast(new WriteTimeoutHandler(timeout, MILLISECONDS));
                }));
    }

}
