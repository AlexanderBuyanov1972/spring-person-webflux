package com.example.springpersonwebflux.security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.csrf.CookieServerCsrfTokenRepository;
import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.security.web.server.csrf.ServerCsrfTokenRequestHandler;
import org.springframework.security.web.server.csrf.XorServerCsrfTokenRequestAttributeHandler;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.web.server.WebFilter;

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
        public final String[] publicRoutes = { "/api/v1/auth/register", "/api/v1/auth/login" };

        @Bean
        SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http,  ReactiveAuthenticationManager authenticationManager) {
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
                                .pathMatchers( "/auth","/auth/**").permitAll()
                                   .anyExchange()
                                .authenticated())
                                // .exceptionHandling(handling -> handling
                                //                 .authenticationEntryPoint((swe, err) -> {
                                //                         log.error("In SequrityWebFilterChain - authorized error",
                                //                                         err.getMessage());
                                //                         return Mono.fromRunnable(
                                //                                         () -> swe.getResponse().setStatusCode(
                                //                                                         HttpStatus.UNAUTHORIZED));
                                //                 })
                                //                 .accessDeniedHandler((swe, err) -> {
                                //                         log.error("In SequrityWebFilterChain - access denied",
                                //                                         err.getMessage());
                                //                         return Mono.fromRunnable(
                                //                                         () -> swe.getResponse().setStatusCode(
                                //                                                         HttpStatus.FORBIDDEN));
                                //                 }))
                                                // .addFilterAfter(bearerAuthenticationFilter(authenticationManager),
                                                // SecurityWebFiltersOrder.AUTHENTICATION)
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
        ReactiveAuthenticationManager authenticationManager(){
            return authenticate -> Mono.empty();
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
