package com.example.springpersonwebflux.security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;

import com.example.springpersonwebflux.security.BearerTokenServerAuthenticationConverter;
import com.example.springpersonwebflux.security.JwtHandler;

import reactor.core.publisher.Mono;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableWebFluxSecurity
public class WebSecurityConfig {

    @Value("${jwt.secret}")
    private String secret;
    public final String[] publicRoutes = {"/auth/register", "/auth/login","/user", "/user/**"};

    @Bean
    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http, ReactiveAuthenticationManager authenticationManager) {
        http.csrf(ServerHttpSecurity.CsrfSpec::disable);

        http.authorizeExchange(exchange -> exchange
                        .pathMatchers(publicRoutes).permitAll()
                        .anyExchange()
                        .authenticated())
                .exceptionHandling(handling -> handling
                        .authenticationEntryPoint((swe, err) -> {
                            log.error("In SecurityWebFilterChain - authorized error",
                                    err.getMessage());
                            return Mono.fromRunnable(
                                    () -> swe.getResponse().setStatusCode(
                                            HttpStatus.UNAUTHORIZED));
                        })
                        .accessDeniedHandler((swe, err) -> {
                            log.error("In SecurityWebFilterChain - access denied",
                                    err.getMessage());
                            return Mono.fromRunnable(
                                    () -> swe.getResponse().setStatusCode(
                                            HttpStatus.FORBIDDEN));
                        }))
                .addFilterAfter(bearerAuthenticationFilter(authenticationManager),
                        SecurityWebFiltersOrder.AUTHENTICATION)
        ;

        return http.build();
    }

    private AuthenticationWebFilter bearerAuthenticationFilter(
            ReactiveAuthenticationManager authenticationManager) {
        AuthenticationWebFilter bearerAuthenticationFilter = new AuthenticationWebFilter(authenticationManager);
        bearerAuthenticationFilter
                .setServerAuthenticationConverter(
                        new BearerTokenServerAuthenticationConverter(new JwtHandler(secret)));
        bearerAuthenticationFilter
                .setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers("/**"));
        return bearerAuthenticationFilter;
    }

    @Bean
    ReactiveAuthenticationManager authenticationManager() {
        return authenticate -> Mono.empty();
    }


}
