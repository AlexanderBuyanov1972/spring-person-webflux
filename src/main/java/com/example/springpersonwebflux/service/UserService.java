package com.example.springpersonwebflux.service;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.springpersonwebflux.dao.UserRepository;
import com.example.springpersonwebflux.entities.UserEntity;
import com.example.springpersonwebflux.entities.UserRole;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements IUserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public Mono<UserEntity> registerUser(UserEntity userEntity) {
        return userRepository.save(
                userEntity.toBuilder()
                        .password(passwordEncoder.encode(userEntity.getPassword()))
                        .enabled(true)
                        .role(UserRole.USER)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build())
                .doOnSuccess(u -> {
                    log.info("In register user - user: {} created", u);
                });

    }

    public Mono<UserEntity> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Mono<UserEntity> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Mono<UserEntity> createUser(UserEntity user) {
        Mono<UserEntity> one = userRepository.save(user);
        return one;
    }

    @Override
    public Mono<UserEntity> updateUser(UserEntity user) {
        return userRepository.save(user);
    }

    @Override
    public Mono<UserEntity> getUser(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public  Mono<UserEntity> deleteUser(Long id) {
        Mono<UserEntity> user = userRepository.findById(id);
        if(user != null){
            userRepository.deleteById(id);
            return user;
        }
        return Mono.empty();
         
    }

    @Override
    public Flux<UserEntity> getAllUser() {
        return userRepository.findAll();
    }

}