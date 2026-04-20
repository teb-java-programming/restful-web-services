package com.teb.practice.service.mfa;

import com.teb.practice.entity.User;
import com.teb.practice.repository.UserRepository;

import dev.samstevens.totp.code.CodeGenerator;
import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.code.DefaultCodeVerifier;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MfaService {

    private final UserRepository userRepository;
    private final SecretGenerator secretGenerator = new DefaultSecretGenerator();
    private final TimeProvider timeProvider = new SystemTimeProvider();
    private final CodeGenerator codeGenerator = new DefaultCodeGenerator();
    private final CodeVerifier codeVerifier = new DefaultCodeVerifier(codeGenerator, timeProvider);

    public String setupMfa(String email) {

        User user = userRepository.findByEmail(email).orElseThrow();
        String secret = secretGenerator.generate();

        user.setMfaSecret(secret);
        user.setMfaEnabled(true);

        userRepository.save(user);

        return secret;
    }

    public boolean verifyCode(String email, String code) {

        User user = userRepository.findByEmail(email).orElseThrow();

        if (user.getMfaSecret() == null) {
            return false;
        }

        return codeVerifier.isValidCode(user.getMfaSecret(), code);
    }
}
