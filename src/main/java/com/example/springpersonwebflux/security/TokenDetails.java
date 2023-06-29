package com.example.springpersonwebflux.security;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class TokenDetails {
    private Long userId;
    private String token;
    // data creating token
    private Date issuedAt;
    // data no valid token
    private Date expiresAt;

}
