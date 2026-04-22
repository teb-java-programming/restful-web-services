package com.teb.practice.service.session;

import static com.teb.practice.entity.AuditEventType.SESSION_REVOKED;

import static java.time.Duration.ofMillis;
import static java.time.LocalDateTime.now;

import com.teb.practice.cache.UserSession;
import com.teb.practice.exception.SessionException;
import com.teb.practice.repository.UserRepository;
import com.teb.practice.service.audit.AuditLogService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SessionService {

    private static final String PREFIX = "sessions:";

    private final UserRepository userRepository;
    private final AuditLogService auditLogService;
    private final RedisTemplate<String, Object> redisTemplate;

    public void createSession(UserSession session, long ttlMillis) {

        String key = PREFIX + session.getUserId();

        session.setCreatedAt(now());
        session.setExpiryAt(now().plusSeconds(ttlMillis / 1000));

        redisTemplate.opsForHash().put(key, session.getDeviceId(), session);
        redisTemplate.expire(key, ofMillis(ttlMillis));
    }

    public UserSession getSession(Long userId) {

        return (UserSession)
                redisTemplate.opsForHash().entries(PREFIX + userId).values().stream()
                        .findFirst()
                        .orElse(null);
    }

    public void deleteSession(Long userId) {

        redisTemplate.delete(PREFIX + userId);
    }

    public void updateLastActive(Long userId) {

        UserSession session = getSession(userId);

        if (session != null) {
            session.setLastActiveAt(now());
            createSession(session, 7 * 24 * 60 * 60 * 1000L);
        }
    }

    public List<UserSession> getAllSessions(Long userId) {

        List<UserSession> sessions = new ArrayList<>();

        for (Object value : redisTemplate.opsForHash().entries(PREFIX + userId).values()) {
            sessions.add((UserSession) value);
        }

        return sessions;
    }

    public List<UserSession> getAllSessionsByEmail(String email) {

        return getAllSessions(userRepository.findByEmail(email).orElseThrow().getId());
    }

    public void saveSession(UserSession session, long ttl) {

        String key = PREFIX + session.getUserId();

        redisTemplate.opsForHash().put(key, session.getDeviceId(), session);
        redisTemplate.expire(key, ofMillis(ttl));
    }

    public void deleteSessionByDevice(Long userId, String deviceId) {

        Long deleted = redisTemplate.opsForHash().delete(PREFIX + userId, deviceId);

        if (deleted == null || deleted == 0) {
            throw new SessionException("Device session not found");
        }

        auditLogService.logEvent(
                userId, SESSION_REVOKED, "Device session revoked: " + deviceId, null, null);
    }
}
