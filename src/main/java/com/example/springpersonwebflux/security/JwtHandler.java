package com.example.springpersonwebflux.security;

import java.util.Date;

import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.base64.Base64;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import com.example.springpersonwebflux.exceptions.UnauthorizeException;
import reactor.core.publisher.Mono;

public class JwtHandler {

    private final String secret;

    public JwtHandler(String secret) {
        this.secret = secret;
    }

    public Mono<VerificationResult> check(String accessToken) {
        return Mono.just(verify(accessToken))
                .onErrorResume(e -> Mono.error(new UnauthorizeException(e.getMessage())));
    }

    private VerificationResult verify(String token) {
        Claims claims = getClaimsFromToken(token);
        final Date expirationDate = claims.getExpiration();

        if (expirationDate.before(new Date())) {
            throw new RuntimeException("Token expire");
        }
        return new VerificationResult(claims, token);

    }

    private Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(Base64.encode(secret.getBytes()))
                .parseClaimsJws(token)
                .getBody();
    }

    public static class VerificationResult {
        public Claims claims;
        public String token;

        public VerificationResult(Claims claims, String token) {
            this.claims = claims;
            this.token = token;
        }

    }

}
