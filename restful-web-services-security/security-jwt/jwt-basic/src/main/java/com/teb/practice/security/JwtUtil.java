package com.teb.practice.security;

import static io.jsonwebtoken.security.Keys.hmacShaKeyFor;

import static java.lang.System.currentTimeMillis;
import static java.util.Base64.getDecoder;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

@Component
public class JwtUtil {

    private static final String TOKEN_TYPE = "access";

    private final SecretKey key;
    private final long expiration;
    private final String issuer;

    public JwtUtil(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expiration,
            @Value("${jwt.issuer}") String issuer) {
        this.key = hmacShaKeyFor(getDecoder().decode(secret));
        this.expiration = expiration;
        this.issuer = issuer;
    }

    public String generateToken(String username, List<String> roles) {

        return Jwts.builder()
                .subject(username)
                .claim("roles", roles)
                .claim("type", TOKEN_TYPE)
                .issuer(issuer)
                .issuedAt(new Date())
                .expiration(new Date(currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
    }

    public Claims extractClaims(String token) {

        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    }

    public String extractUsername(String token) {

        return extractClaims(token).getSubject();
    }

    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token) {

        return (List<String>) extractClaims(token).get("roles");
    }

    public boolean isValid(String token) {

        try {
            extractClaims(token);

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
