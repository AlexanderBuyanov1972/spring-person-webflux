package com.example.springpersonwebflux.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springpersonwebflux.dao.PersonRepository;
import com.example.springpersonwebflux.entities.Person;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ServicePerson implements IServicePerson {
    @Autowired
    private PersonRepository repository;

    @Override
    public Mono<Person> createPerson(Person person) {
        return repository.create(person);
    }

    @Override
    public Mono<Person> updatePerson(Person person) {
        return repository.update(person);
    }

    @Override
    public Mono<Person> getPerson(String id) {
        return repository.findById(id);
    }

    @Override
    public Mono<Person> deletePerson(String id) {
        return repository.removeById(id);
    }

    @Override
    public Flux<Person> getAllPerson() {
        return repository.findAll();
    }

}