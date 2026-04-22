package com.teb.practice.controller;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.security.core.context.SecurityContextHolder.clearContext;

import com.teb.practice.exception.InvalidCredentialsException;
import com.teb.practice.request.AuthRequest;
import com.teb.practice.response.ApiResponse;
import com.teb.practice.security.JwtUtil;
import com.teb.practice.service.InMemoryUserService;
import com.teb.practice.service.InMemoryUserService.UserRecord;
import com.teb.practice.service.auth.RefreshTokenService;
import com.teb.practice.token.TokenBlacklist;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final InMemoryUserService userService;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final TokenBlacklist tokenBlacklist;

    public AuthController(
            InMemoryUserService userService,
            JwtUtil jwtUtil,
            RefreshTokenService refreshTokenService,
            TokenBlacklist tokenBlacklist) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.refreshTokenService = refreshTokenService;
        this.tokenBlacklist = tokenBlacklist;
    }

    // Login issues both access token + refresh token
    @PostMapping("/login")
    @ResponseStatus(OK)
    public ApiResponse<Map<String, String>> login(@RequestBody AuthRequest request) {

        UserRecord user = userService.validate(request.username(), request.password());

        if (user == null) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        return new ApiResponse<>(
                Map.of(
                        "accessToken", jwtUtil.generateToken(user.username(), user.roles()),
                        "refreshToken", refreshTokenService.create(user.username()).token()));
    }

    // Refresh issues new access token using refresh token
    @PostMapping("/refresh")
    @ResponseStatus(OK)
    public ApiResponse<String> refresh(@RequestParam String refreshToken) {

        String username = refreshTokenService.validate(refreshToken);

        if (username == null) {
            throw new InvalidCredentialsException("Invalid refresh token");
        }

        UserRecord user = userService.findByUsername(username);

        return new ApiResponse<>(jwtUtil.generateToken(user.username(), user.roles()));
    }

    @PostMapping("/logout")
    @ResponseStatus(OK)
    public ApiResponse<String> logout(
            @RequestParam String accessToken, @RequestParam(required = false) String refreshToken) {

        if (refreshToken != null) {
            refreshTokenService.revoke(refreshToken);
        }

        tokenBlacklist.revoke(accessToken);
        clearContext();

        return new ApiResponse<>("Logged out");
    }
}
