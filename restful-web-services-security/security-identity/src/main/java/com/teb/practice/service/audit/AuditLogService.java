package com.teb.practice.service.audit;

import static java.time.LocalDateTime.now;

import com.teb.practice.entity.AuditEventType;
import com.teb.practice.entity.AuditLog;
import com.teb.practice.repository.AuditLogRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public void logEvent(
            Long userId,
            AuditEventType eventType,
            String message,
            String ipAddress,
            String userAgent) {

        auditLogRepository.save(
                AuditLog.builder()
                        .userId(userId)
                        .eventType(eventType)
                        .message(message)
                        .ipAddress(ipAddress)
                        .userAgent(userAgent)
                        .createdAt(now())
                        .build());
    }

    public List<AuditLog> getLogsByUserId(Long userId) {

        return auditLogRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }
}
