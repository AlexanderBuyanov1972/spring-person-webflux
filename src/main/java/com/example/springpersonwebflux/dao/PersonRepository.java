package com.example.springpersonwebflux.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.example.springpersonwebflux.entities.Person;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class PersonRepository {
    private Map<String, Person> repository = new HashMap<String, Person>();

    public PersonRepository() {
        repository.put("1", new Person("1", "Person_1", 21));
        repository.put("2", new Person("2", "Person_2", 22));
        repository.put("3", new Person("3", "Person_3", 23));
        repository.put("4", new Person("4", "Person_4", 24));
        repository.put("5", new Person("5", "Person_5", 25));
        repository.put("6", new Person("6", "Person_6", 26));
        repository.put("7", new Person("7", "Person_7", 27));
        repository.put("8", new Person("8", "Person_8", 28));
        repository.put("9", new Person("9", "Person_9", 29));
        repository.put("10", new Person("10", "Person_10", 30));
    }

    public Mono<Person> create(Person person) {
        for (String key : repository.keySet()) {
            Person one = repository.get(key);
            if (one.getId().equals(person.getId())) {
                return Mono.just(new Person());
            }
        }
        repository.put(repository.size() + 1 + "", person);
        return Mono.just(person);
    }

    public Mono<Person> update(Person person) {
        for (String key : repository.keySet()) {
            Person one = repository.get(key);
            if (one.getId().equals(person.getId())) {
                repository.put(key, person);
                return Mono.just(person);
            }
        }
        return Mono.just(new Person());
    }

    public Mono<Person> findById(String id) {
        for (String key : repository.keySet()) {
            Person one = repository.get(key);
            if (one.getId().equals(id)) {
                return Mono.just(one);
            }
        }
        return Mono.just(new Person());
    }

    public Mono<Person> removeById(String id) {
        for (String key : repository.keySet()) {
            Person one = repository.get(key);
            if (one.getId().equals(id)) {
                repository.remove(key);
                return Mono.just(one);
            }
        }
        return Mono.just(new Person());
    }

    public Flux<Person> findAll() {
        return Flux.fromIterable(repository.values());
    }
}
