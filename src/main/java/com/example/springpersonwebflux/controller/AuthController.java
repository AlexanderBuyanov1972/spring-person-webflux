package com.example.springpersonwebflux.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springpersonwebflux.dto.AuthRequestDto;
import com.example.springpersonwebflux.dto.AuthResponseDto;
import com.example.springpersonwebflux.dto.UserDto;
import com.example.springpersonwebflux.entities.UserEntity;
import com.example.springpersonwebflux.mapper.UserMapper;
import com.example.springpersonwebflux.security.CustomPrincipal;
import com.example.springpersonwebflux.security.SecurityService;
import com.example.springpersonwebflux.service.UserService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final SecurityService securityService;
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/register")
    public Mono<UserDto> registration(@RequestBody UserDto userDto) {
        System.out.println("/register");
        UserEntity userEntity = userMapper.map(userDto);
        return userService.registerUser(userEntity)
                .map(userMapper::map);
    }

    @PostMapping("/login")
    public Mono<AuthResponseDto> login(@RequestBody AuthRequestDto authRequestDto) {
        return securityService.authenticate(authRequestDto.getUsername(), authRequestDto.getPassword())
                .flatMap(tokenDetails -> Mono.just(
                        AuthResponseDto.builder()
                                .userId(tokenDetails.getUserId())
                                .token(tokenDetails.getToken())
                                .issuedAt(tokenDetails.getIssuedAt())
                                .expiresAt(tokenDetails.getExpiresAt())
                                .build()));
    }

    @GetMapping("/info")
    public Mono<UserDto> info(Authentication authentication) {
    CustomPrincipal customPrincipal = (CustomPrincipal)
    authentication.getPrincipal();
    return userService.getUserById(customPrincipal.getId())
    .map(userMapper::map);

    }

    }