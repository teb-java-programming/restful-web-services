package com.teb.practice.service.auth;

import static com.teb.practice.entity.AuditEventType.LOGIN_SUCCESS;
import static com.teb.practice.entity.AuditEventType.LOGOUT;
import static com.teb.practice.entity.AuditEventType.MFA_FAILED;
import static com.teb.practice.entity.AuditEventType.MFA_REQUIRED;
import static com.teb.practice.entity.AuditEventType.MFA_SUCCESS;
import static com.teb.practice.entity.AuditEventType.TOKEN_REFRESH;
import static com.teb.practice.entity.AuditEventType.TOKEN_REFRESH_FAILED;

import static java.time.LocalDateTime.now;
import static java.util.UUID.randomUUID;

import com.teb.practice.dto.request.LoginRequest;
import com.teb.practice.dto.request.MfaVerifyRequest;
import com.teb.practice.dto.request.RefreshTokenRequest;
import com.teb.practice.dto.request.RegisterRequest;
import com.teb.practice.dto.response.AuthResponse;
import com.teb.practice.entity.RefreshToken;
import com.teb.practice.entity.User;
import com.teb.practice.exception.AuthException;
import com.teb.practice.exception.MfaException;
import com.teb.practice.repository.RefreshTokenRepository;
import com.teb.practice.repository.UserRepository;
import com.teb.practice.security.jwt.JwtService;
import com.teb.practice.service.audit.AuditLogService;
import com.teb.practice.service.mfa.MfaService;
import com.teb.practice.service.session.SessionService;
import com.teb.practice.cache.UserSession;

import jakarta.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final Long WEEK_DURATION_MS = 7 * 24 * 60 * 60 * 1000L;

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final SessionService sessionService;
    private final MfaService mfaService;
    private final AuditLogService auditLogService;

    public void register(RegisterRequest request) {

        userRepository.save(
                User.builder()
                        .username(request.username())
                        .email(request.email())
                        .password(passwordEncoder.encode(request.password()))
                        .role("USER")
                        .enabled(true)
                        .build());
    }

    public AuthResponse login(LoginRequest request, HttpServletRequest httpRequest) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        User user = userRepository.findByEmail(request.email()).orElseThrow();

        if (user.isMfaEnabled()) {
            return new AuthResponse(null, null, MFA_REQUIRED.name());
        }

        String refreshTokenValue = jwtService.generateRefreshToken(user.getEmail());

        saveRefreshToken(user, refreshTokenValue);

        Long userId = user.getId();
        String ip = httpRequest.getRemoteAddr();
        String userAgent = httpRequest.getHeader("User-Agent");
        String deviceId = randomUUID().toString();

        UserSession session =
                UserSession.builder()
                        .userId(userId)
                        .email(user.getEmail())
                        .refreshToken(refreshTokenValue)
                        .deviceId(deviceId)
                        .ipAddress(ip)
                        .userAgent(userAgent)
                        .lastActiveAt(now())
                        .build();

        sessionService.saveSession(session, WEEK_DURATION_MS);

        sessionService.updateLastActive(userId);

        auditLogService.logEvent(userId, LOGIN_SUCCESS, "User logged in", ip, userAgent);

        return new AuthResponse(
                jwtService.generateAccessToken(user.getEmail()), refreshTokenValue, "Bearer");
    }

    public AuthResponse refresh(RefreshTokenRequest request) {

        RefreshToken storedToken =
                refreshTokenRepository.findByToken(request.refreshToken()).orElseThrow();

        if (storedToken.isRevoked() || storedToken.getExpiryDate().isBefore(now())) {
            auditLogService.logEvent(
                    null, TOKEN_REFRESH_FAILED, "Expired or revoked refresh token", null, null);

            throw new AuthException("Invalid refresh token");
        }

        String storedTokenEmail = storedToken.getUser().getEmail();
        Long storedTokenUserId = storedToken.getUser().getId();
        UserSession session = sessionService.getSession(storedTokenUserId);

        if (session == null || !session.getRefreshToken().equals(request.refreshToken())) {
            auditLogService.logEvent(
                    storedTokenUserId,
                    TOKEN_REFRESH_FAILED,
                    "Refresh token mismatch or session invalid",
                    null,
                    null);

            throw new AuthException("Session invalid");
        }

        String newAccessToken = jwtService.generateAccessToken(storedTokenEmail);
        String newRefreshToken = jwtService.generateRefreshToken(storedTokenEmail);

        storedToken.setRevoked(true);
        refreshTokenRepository.save(storedToken);

        saveRefreshToken(storedToken.getUser(), newRefreshToken);

        session.setRefreshToken(newRefreshToken);
        session.setLastActiveAt(now());

        sessionService.saveSession(session, WEEK_DURATION_MS);

        auditLogService.logEvent(
                storedTokenUserId, TOKEN_REFRESH, "Access token refreshed", null, null);

        return new AuthResponse(newAccessToken, newRefreshToken, "Bearer");
    }

    public void logout(String refreshToken) {

        RefreshToken token = refreshTokenRepository.findByToken(refreshToken).orElseThrow();

        token.setRevoked(true);
        refreshTokenRepository.save(token);
        sessionService.deleteSession(token.getUser().getId());

        auditLogService.logEvent(token.getUser().getId(), LOGOUT, "User logged out", null, null);
    }

    public AuthResponse verifyMfa(MfaVerifyRequest request) {

        boolean valid = mfaService.verifyCode(request.email(), request.code());

        if (!valid) {
            auditLogService.logEvent(
                    null, MFA_FAILED, "Invalid MFA code for " + request.email(), null, null);

            throw new MfaException("Invalid MFA code");
        }

        User user = userRepository.findByEmail(request.email()).orElseThrow();

        String accessToken = jwtService.generateAccessToken(user.getEmail());
        String refreshTokenValue = jwtService.generateRefreshToken(user.getEmail());

        saveRefreshToken(user, refreshTokenValue);

        sessionService.createSession(
                UserSession.builder()
                        .userId(user.getId())
                        .email(user.getEmail())
                        .refreshToken(refreshTokenValue)
                        .deviceId("default")
                        .build(),
                WEEK_DURATION_MS);
        auditLogService.logEvent(user.getId(), MFA_SUCCESS, "MFA verified", null, null);

        return new AuthResponse(accessToken, refreshTokenValue, "Bearer");
    }

    private void saveRefreshToken(User user, String token) {

        refreshTokenRepository.save(
                RefreshToken.builder()
                        .user(user)
                        .token(token)
                        .expiryDate(now().plusDays(7))
                        .revoked(false)
                        .build());
    }
}
