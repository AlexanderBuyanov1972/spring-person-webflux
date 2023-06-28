package com.example.springpersonwebflux.dao;


import com.example.springpersonwebflux.entities.UserEntity;

import reactor.core.publisher.Mono;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface UserRepository extends R2dbcRepository<UserEntity, Long> {
    Mono<UserEntity> findByUsername(String username);

}
