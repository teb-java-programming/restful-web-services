package com.teb.practice.token;

import java.time.Instant;

// Generates a long-lived refresh token tied to a user
public record RefreshToken(String token, String username, Instant expiry) {}
