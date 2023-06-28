package com.example.springpersonwebflux.service;

import com.example.springpersonwebflux.entities.UserEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IServiceUser {
    Mono<UserEntity> createUser(UserEntity user);

    Mono<UserEntity> updateUser(UserEntity user);

    Mono<UserEntity> getUser(Long id);

    Mono<UserEntity> deleteUser(Long id);

    Flux<UserEntity> getAllUser();
}
