package co.com.bancolombia.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
public class JwtSecurityConfig {
    private final String issuer;
    private final String jwkSetUri;
    private final String subject;

    public JwtSecurityConfig(
            @Value("${spring.security.oauth2.resourceserver.jwt.issuer}") String issuer,
            @Value("${spring.security.oauth2.resourceserver.jwt.subject}") String subject,
            @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}") String jwkSetUri
    ){
        this.issuer = issuer;
        this.subject = subject;
        this.jwkSetUri = jwkSetUri;
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http, ReactiveJwtDecoder jwtDecoder) {
        http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges.pathMatchers("/health")
                        .permitAll()
                        .pathMatchers(HttpMethod.OPTIONS)
                        .permitAll()
                        .anyExchange()
                        .authenticated())
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt -> jwt.jwtDecoder(jwtDecoder)
                                //.jwtAuthenticationConverter(reactiveJwtAuthenticationConverter())
                        )
                );
        return http.build();
    }

    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
        NimbusReactiveJwtDecoder decoder = NimbusReactiveJwtDecoder.withJwkSetUri(jwkSetUri).build();

        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuer);
        OAuth2TokenValidator<Jwt> withSubject = JwtValidators.createDefaultWithValidators(token -> {
            if (token.getSubject().contains(subject)) {
                return OAuth2TokenValidatorResult.success();
            }
            OAuth2Error err = new OAuth2Error("invalid_token", "The required subject is missing", null);
            return OAuth2TokenValidatorResult.failure(err);
        });

        OAuth2TokenValidator<Jwt> validator = new DelegatingOAuth2TokenValidator<>(withIssuer, withSubject);
        decoder.setJwtValidator(validator);
        return decoder;
    }

    // Convert Jwt claims -> GrantedAuthorities (customize claim name/path as needed)
    private Converter<Jwt, Mono<AbstractAuthenticationToken>> reactiveJwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();

        // default JwtGrantedAuthoritiesConverter reads "scope" -> "SCOPE_xxx"
        // If your roles are in another claim (e.g. "roles"), customize like:
        JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        // authoritiesConverter.setAuthorityPrefix("ROLE_"); // optional
        // authoritiesConverter.setAuthoritiesClaimName("roles"); // if roles live in `roles` claim

        jwtConverter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);

        // adapt imperative converter to reactive expected type
        return new ReactiveJwtAuthenticationConverterAdapter(jwtConverter);
    }
}