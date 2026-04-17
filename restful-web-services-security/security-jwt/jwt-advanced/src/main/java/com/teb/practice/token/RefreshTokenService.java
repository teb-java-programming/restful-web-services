package com.teb.practice.token;

import static java.time.Instant.now;
import static java.util.UUID.randomUUID;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public String create(String username) {

        RefreshToken token =
                RefreshToken.builder()
                        .token(randomUUID().toString())
                        .username(username)
                        .expiry(now().plusSeconds(60 * 60 * 24))
                        .build();

        refreshTokenRepository.save(token);

        return token.getToken();
    }

    public String validate(String token) {

        RefreshToken refreshToken = refreshTokenRepository.findByToken(token).orElse(null);

        if (refreshToken == null || refreshToken.getExpiry().isBefore(now())) {
            return null;
        }

        return refreshToken.getUsername();
    }

    public String rotate(String refreshToken) {

        String username = validate(refreshToken);

        if (username == null) {
            return null;
        }

        revoke(refreshToken);

        return username;
    }

    public void revoke(String token) {

        refreshTokenRepository.deleteByToken(token);
    }
}
