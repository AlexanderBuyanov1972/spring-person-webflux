package com.example.springpersonwebflux.security;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import com.example.springpersonwebflux.entities.UserEntity;
import com.example.springpersonwebflux.exceptions.AuthException;
import com.example.springpersonwebflux.service.UserService;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class SecurityService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Integer expirationInSecond;
    @Value("${jwt.issuer}")
    private String issuer;

    private TokenDetails generationToken(UserEntity user) {
        Map<String, Object> claims = new HashMap<>() {
            {
                put("role", user.getRole());
                put("username", user.getUsername());
            }
        };
        return generationToken(claims, user.getId().toString());
    }

    private TokenDetails generationToken(Map<String, Object> claims, String subject) {
        Long expirationTimeInMillisecond = expirationInSecond + 1000L;
        Date expirationDate = new Date(new Date().getTime() + expirationTimeInMillisecond);
        return generationToken(expirationDate, claims, subject);
    }

    private TokenDetails generationToken(Date expirationDate, Map<String, Object> claims, String subject) {
        Date createdDate = new Date();

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuer(issuer)
                .setSubject(subject)
                .setIssuedAt(createdDate)
                .setId(UUID.randomUUID().toString())
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, Base64.getEncoder().encodeToString(secret.getBytes()))
                .compact();

        return TokenDetails.builder()
                .token(token)
                .issuedAt(createdDate)
                .expiresAt(expirationDate)
                .build();

    }

    public Mono<TokenDetails> authenticate(String username, String password) {
        return userService.getUserByUsername(username)
                .flatMap(user -> {
                    if (!user.isEnabled()) {
                        return Mono.error(new AuthException("Account disabled", "USER_ACCOUNT_DISABLED"));
                    }
                    if (!passwordEncoder.matches(password, user.getPassword())) {
                        return Mono.error(new AuthException("Invalid password", "USER_PASSWORD_INVALID"));
                    }
                    return Mono.just(generationToken(user)
                            .toBuilder()
                            .userId(user.getId())
                            .build());
                })
                .switchIfEmpty(Mono.error(new AuthException("Username invalid", "USERNAME_INVALID")));
    }

}