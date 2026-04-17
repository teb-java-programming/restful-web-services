package com.teb.practice.token;

import static java.util.concurrent.ConcurrentHashMap.newKeySet;

import org.springframework.stereotype.Component;

import java.util.Set;

// Stores invalidated JWT access tokens (in-memory blacklist)
@Component
public class TokenBlacklist {

    private final Set<String> blacklist = newKeySet();

    // Marks token as invalid (used during logout)
    public void revoke(String token) {

        blacklist.add(token);
    }

    // Checks if token is revoked
    public boolean isRevoked(String token) {

        return blacklist.contains(token);
    }
}
