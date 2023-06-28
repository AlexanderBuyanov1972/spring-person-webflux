package com.example.springpersonwebflux.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.springpersonwebflux.entities.Person;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class PersonWebClient {
    WebClient client = WebClient.create("http://localhost:8080");
    private static final Logger LOGGER = LoggerFactory.getLogger(PersonWebClient.class);

    public void consume() {

        Mono<Person> personMono = client.get()
                .uri("/person/{id}", "1")
                .retrieve()
                .bodyToMono(Person.class);

        personMono.subscribe(person -> LOGGER.info("Person: {}", person));

        Flux<Person> personFlux = client.get()
                .uri("/person")
                .retrieve()
                .bodyToFlux(Person.class);

        personFlux.subscribe(person -> LOGGER.info("Person: {}", person));
    }
}
