package com.example.springpersonwebflux.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.csrf.CookieServerCsrfTokenRepository;
import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.security.web.server.csrf.ServerCsrfTokenRequestHandler;
import org.springframework.security.web.server.csrf.XorServerCsrfTokenRequestAttributeHandler;
import org.springframework.web.server.WebFilter;

import reactor.core.publisher.Mono;

import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;

@Configuration
@EnableWebFluxSecurity
public class WebSecurityConfig {

        @Bean
        SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
                CookieServerCsrfTokenRepository tokenRepository = CookieServerCsrfTokenRepository.withHttpOnlyFalse();
                XorServerCsrfTokenRequestAttributeHandler delegate = new XorServerCsrfTokenRequestAttributeHandler();
                // Use only the handle() method of XorServerCsrfTokenRequestAttributeHandler and
                // the
                // default implementation of resolveCsrfTokenValue() from
                // ServerCsrfTokenRequestHandler
                ServerCsrfTokenRequestHandler requestHandler = delegate::handle;
                http.csrf((csrf) -> csrf
                                .csrfTokenRepository(tokenRepository)
                                .csrfTokenRequestHandler(requestHandler));
                http.authorizeExchange(exchange -> exchange
                                .pathMatchers(HttpMethod.POST, "/person").permitAll()
                                .pathMatchers(HttpMethod.PUT, "/person").permitAll()
                                .pathMatchers(HttpMethod.GET, "/person/**").permitAll()
                                .pathMatchers(HttpMethod.DELETE, "/person/**")
                                .permitAll());

                return http.build();
        }

        @Bean
        WebFilter csrfCookieWebFilter() {
                return (exchange, chain) -> {
                        Mono<CsrfToken> csrfToken = exchange.getAttributeOrDefault(CsrfToken.class.getName(),
                                        Mono.empty());
                        return csrfToken.doOnSuccess(token -> {
                                /* Ensures the token is subscribed to. */
                        }).then(chain.filter(exchange));
                };
        }

}
