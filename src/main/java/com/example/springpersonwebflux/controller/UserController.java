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

import com.example.springpersonwebflux.entities.UserEntity;
import com.example.springpersonwebflux.service.IServiceUser;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth")
public class UserController {
    @Autowired
    private IServiceUser serviceUser;

    @PostMapping
    public Mono<UserEntity> create(@RequestBody UserEntity user) {
        return serviceUser.createUser(user);
    }

    @PutMapping
    public Mono<UserEntity> update(@RequestBody UserEntity user) {
        return serviceUser.updateUser(user);
    }

    @GetMapping("/{id}")
    public Mono<UserEntity> getPerson(@PathVariable Long id) {
        return serviceUser.getUser(id);
    }

    @DeleteMapping("/{id}")
    public Mono<UserEntity> deletePerson(@PathVariable Long id) {
        return serviceUser.deleteUser(id);
    }

    @GetMapping("/all")
    public Flux<UserEntity> getAllEmployees() {
        return serviceUser.getAllUser();
    }
}