package com.teb.practice.service.auth;

import static java.time.Instant.now;
import static java.util.UUID.randomUUID;

import com.teb.practice.token.RefreshToken;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// Manages refresh tokens (in-memory store for now)
@Service
public class RefreshTokenService {

    private final long refreshValidityInMillis;

    private final Map<String, RefreshToken> store = new ConcurrentHashMap<>();

    public RefreshTokenService(@Value("${jwt.refresh.expiration}") long refreshValidityInMillis) {
        this.refreshValidityInMillis = refreshValidityInMillis;
    }

    // Generates a new refresh token for a user
    public RefreshToken create(String username) {

        String token = randomUUID().toString();

        RefreshToken refreshToken =
                new RefreshToken(token, username, now().plusMillis(refreshValidityInMillis));

        store.put(token, refreshToken);

        return refreshToken;
    }

    // Validates refresh token and returns associated user if valid
    public String validate(String token) {

        RefreshToken refreshToken = store.get(token);

        if (refreshToken == null) {
            return null;
        }

        if (refreshToken.expiry().isBefore(now())) {
            store.remove(token);

            return null;
        }

        return refreshToken.username();
    }

    // Removes refresh token (used for logout later)
    public void revoke(String token) {

        store.remove(token);
    }
}
