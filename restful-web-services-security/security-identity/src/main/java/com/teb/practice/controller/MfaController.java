package com.teb.practice.controller;

import static org.springframework.http.ResponseEntity.ok;

import com.teb.practice.dto.request.MfaVerifyRequest;
import com.teb.practice.dto.response.AuthResponse;
import com.teb.practice.service.auth.AuthService;
import com.teb.practice.service.mfa.MfaService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/mfa")
@RequiredArgsConstructor
public class MfaController {

    private final MfaService mfaService;
    private final AuthService authService;

    @PostMapping("/setup")
    public ResponseEntity<?> setup(Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(mfaService.setupMfa(email));
    }

    @PostMapping("/verify")
    public ResponseEntity<AuthResponse> verify(@Valid @RequestBody MfaVerifyRequest request) {

        return ok(authService.verifyMfa(request));
    }
}
