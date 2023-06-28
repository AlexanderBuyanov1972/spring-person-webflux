package com.example.springpersonwebflux.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.springpersonwebflux.entities.UserEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class UserWebClient {
    WebClient client = WebClient.create("http://localhost:8080");
    private static final Logger LOGGER = LoggerFactory.getLogger(UserWebClient.class);

    public void consume() {

        Mono<UserEntity> userMono = client.get()
                .uri("/auth/{id}", 1)
                .retrieve()
                .bodyToMono(UserEntity.class);

        userMono.subscribe(user -> LOGGER.info("UserEntity: {}", user));

        Flux<UserEntity> userFlux = client.get()
                .uri("/auth/all")
                .retrieve()
                .bodyToFlux(UserEntity.class);

        userFlux.subscribe(user -> LOGGER.info("UserEntity: {}", user));
    }
}
