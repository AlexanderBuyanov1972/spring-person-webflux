package com.example.springpersonwebflux.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springpersonwebflux.entities.Person;
import com.example.springpersonwebflux.service.IServicePerson;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/person")
public class PersonController {
    @Autowired
    private IServicePerson servicePerson;

    @PostMapping
    public Mono<Person> create(@RequestBody Person person) {
        return servicePerson.createPerson(person);
    }

    @PutMapping
    public Mono<Person> update(@RequestBody Person person) {
        return servicePerson.updatePerson(person);
    }

    @GetMapping("/{id}")
    public Mono<Person> getPerson(@PathVariable String id) {
        return servicePerson.getPerson(id);
    }

    @DeleteMapping("/{id}")
    public Mono<Person> deletePerson(@PathVariable String id) {
        return servicePerson.deletePerson(id);
    }

    @GetMapping("/all")
    public Flux<Person> getAllEmployees() {
        return servicePerson.getAllPerson();
    }
}