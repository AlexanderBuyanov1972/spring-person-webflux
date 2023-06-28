package com.example.springpersonwebflux.service;

import com.example.springpersonwebflux.entities.Person;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IServicePerson {
    Mono<Person> createPerson(Person person);

    Mono<Person> updatePerson(Person person);

    Mono<Person> getPerson(String id);

    Mono<Person> deletePerson(String id);

    Flux<Person> getAllPerson();
}
