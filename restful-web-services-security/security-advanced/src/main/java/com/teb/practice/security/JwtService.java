package com.teb.practice.security;

import static io.jsonwebtoken.Jwts.builder;
import static io.jsonwebtoken.Jwts.parser;
import static io.jsonwebtoken.security.Keys.hmacShaKeyFor;

import static java.lang.System.currentTimeMillis;
import static java.util.UUID.randomUUID;

import com.teb.practice.user.User;

import io.jsonwebtoken.Claims;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

import javax.crypto.SecretKey;

@Service
public class JwtService {

    private final SecretKey key;
    private final long expiration;
    private final String issuer;

    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expiration,
            @Value("${jwt.issuer}") String issuer) {
        this.key = hmacShaKeyFor(secret.getBytes());
        this.expiration = expiration;
        this.issuer = issuer;
    }

    public String generateAccessToken(User user) {

        return builder()
                .id(randomUUID().toString())
                .subject(user.getUsername())
                .claim("ver", user.getTokenVersion())
                .issuer(issuer)
                .issuedAt(new Date())
                .expiration(new Date(currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
    }

    public Claims extractClaims(String token) {

        return parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    }

    public boolean isValid(String token) {

        var claims = extractClaims(token);

        if (claims.getExpiration().before(new Date())) {
            return false;
        }

        return issuer.equals(claims.getIssuer());
    }

    public String extractJti(String token) {

        return extractClaims(token).getId();
    }
}
