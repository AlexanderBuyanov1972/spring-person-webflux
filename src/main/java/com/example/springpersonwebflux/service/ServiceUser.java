package com.example.springpersonwebflux.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springpersonwebflux.dao.UserRepository;
import com.example.springpersonwebflux.entities.UserEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ServiceUser implements IServiceUser {
    @Autowired
    private UserRepository repository;

    @Override
    public Mono<UserEntity> createUser(UserEntity user) {
        Mono<UserEntity> one = repository.save(user);
        return one;
    }

    @Override
    public Mono<UserEntity> updateUser(UserEntity user) {
        return repository.save(user);
    }

    @Override
    public Mono<UserEntity> getUser(Long id) {
        return repository.findById(id);
    }

    @Override
    public  Mono<UserEntity> deleteUser(Long id) {
        Mono<UserEntity> user = repository.findById(id);
        if(user != null){
            repository.deleteById(id);
            return user;
        }
        return Mono.empty();
         
    }

    @Override
    public Flux<UserEntity> getAllUser() {
        return repository.findAll();
    }

}