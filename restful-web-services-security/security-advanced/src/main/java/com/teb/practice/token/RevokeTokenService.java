package com.teb.practice.token;

import static java.time.Instant.now;

import com.teb.practice.security.JwtService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RevokeTokenService {

    private final RevokeTokenRepository revokeTokenRepository;

    public void revokeAccessToken(String token, JwtService jwtService) {

        revokeTokenRepository.save(new RevokeToken(null, jwtService.extractJti(token), now()));
    }

    public boolean isRevoked(String jti) {

        return revokeTokenRepository.existsByJti(jti);
    }
}
