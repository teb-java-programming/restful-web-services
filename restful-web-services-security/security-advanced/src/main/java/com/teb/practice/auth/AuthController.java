package com.teb.practice.auth;

import static org.springframework.http.HttpStatus.OK;

import com.teb.practice.response.ApiResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @ResponseStatus(OK)
    public ApiResponse<?> login(@RequestBody AuthRequest request) {

        return new ApiResponse<>(authService.login(request));
    }

    @PostMapping("/refresh")
    @ResponseStatus(OK)
    public ApiResponse<?> refresh(@RequestParam String refreshToken) {

        return new ApiResponse<>(authService.refresh(refreshToken));
    }

    @PostMapping("/logout")
    @ResponseStatus(OK)
    public ApiResponse<?> logout(
            @RequestParam String accessToken, @RequestParam(required = false) String refreshToken) {

        authService.logout(accessToken, refreshToken);

        return new ApiResponse<>("Logged out");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/test")
    public String adminOnly() {

        return "Admin access granted";
    }
}
