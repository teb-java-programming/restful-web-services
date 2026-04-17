package com.teb.practice.auth;

import com.teb.practice.exception.InvalidCredentialsException;
import com.teb.practice.exception.UserNotFoundException;
import com.teb.practice.security.JwtService;
import com.teb.practice.token.RefreshTokenService;
import com.teb.practice.token.RevokeTokenService;
import com.teb.practice.user.User;
import com.teb.practice.user.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final RevokeTokenService revokeTokenService;

    public Map<String, String> login(AuthRequest request) {

        User user = checkUserExists(request.username());

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        return mapToken(user);
    }

    public Map<String, String> refresh(String refreshToken) {

        String username = refreshTokenService.rotate(refreshToken);

        if (username == null) {
            throw new InvalidCredentialsException("Invalid refresh token");
        }

        return mapToken(checkUserExists(username));
    }

    public void logout(String accessToken, String refreshToken) {

        if (accessToken != null) {
            revokeTokenService.revokeAccessToken(accessToken, jwtService);
        }

        if (refreshToken != null) {
            refreshTokenService.revoke(refreshToken);
        }
    }

    private User checkUserExists(String username) {

        return userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    private Map<String, String> mapToken(User user) {

        user.setTokenVersion(user.getTokenVersion() + 1);
        userRepository.save(user);

        return Map.of(
                "accessToken", jwtService.generateAccessToken(user),
                "refreshToken", refreshTokenService.create(user.getUsername()));
    }
}
