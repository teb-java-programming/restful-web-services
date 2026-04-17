package com.teb.practice.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class InMemoryUserService {

    // In-memory user store (passwords are hashed using BCrypt)
    private final Map<String, UserRecord> users;
    private final PasswordEncoder passwordEncoder;

    public InMemoryUserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.users =
                Map.of(
                        "user",
                        new UserRecord("user", passwordEncoder.encode("user123"), List.of("USER")),
                        "admin",
                        new UserRecord(
                                "admin", passwordEncoder.encode("admin123"), List.of("ADMIN")));
    }

    // Validates user credentials using BCrypt password matching
    public UserRecord validate(String username, String rawPassword) {

        UserRecord user = users.get(username);

        if (user != null && passwordEncoder.matches(rawPassword, user.password())) {
            return user;
        }

        return null;
    }

    public UserRecord findByUsername(String username) {

        return users.get(username);
    }

    public record UserRecord(String username, String password, List<String> roles) {}
}
