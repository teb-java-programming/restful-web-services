package com.teb.practice.security.jwt;

import static io.jsonwebtoken.Jwts.builder;
import static io.jsonwebtoken.Jwts.parser;
import static io.jsonwebtoken.security.Keys.hmacShaKeyFor;

import static java.lang.System.currentTimeMillis;

import io.jsonwebtoken.Claims;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

@Service
public class JwtService {

    private final String secret;
    private final long accessTokenValidity;
    private final long refreshTokenValidity;
    private final String issuer;

    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long accessTokenValidity,
            @Value("${jwt.refresh.expiration}") long refreshTokenValidity,
            @Value("${jwt.issuer}") String issuer) {
        this.secret = secret;
        this.accessTokenValidity = accessTokenValidity;
        this.refreshTokenValidity = refreshTokenValidity;
        this.issuer = issuer;
    }

    public String generateAccessToken(String email) {

        return generateToken(email, accessTokenValidity);
    }

    public String generateRefreshToken(String email) {

        return generateToken(email, refreshTokenValidity);
    }

    public String extractUsername(String token) {

        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, String email) {

        return extractUsername(token).equals(email) && !isTokenExpired(token);
    }

    private String generateToken(String subject, long validity) {

        return builder()
                .subject(subject)
                .issuer(issuer)
                .issuedAt(new Date())
                .expiration(new Date(currentTimeMillis() + validity))
                .signWith(getSigningKey())
                .compact();
    }

    private SecretKey getSigningKey() {

        return hmacShaKeyFor(secret.getBytes());
    }

    private boolean isTokenExpired(String token) {

        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {

        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> resolver) {

        return resolver.apply(
                parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload());
    }
}
