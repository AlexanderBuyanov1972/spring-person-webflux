package com.example.springpersonwebflux.security;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.example.springpersonwebflux.entities.UserEntity;
import com.example.springpersonwebflux.exceptions.UnauthorizeException;
import com.example.springpersonwebflux.service.UserService;

import lombok.RequiredArgsConstructor;

import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AuthenticationManager implements ReactiveAuthenticationManager {

    private final UserService userService;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();
        return userService.getUserById(principal.getId())
                .filter(UserEntity::isEnabled)
                .switchIfEmpty(Mono.error(new UnauthorizeException("User disabled")))
                .map(user -> authentication);

    }

}
