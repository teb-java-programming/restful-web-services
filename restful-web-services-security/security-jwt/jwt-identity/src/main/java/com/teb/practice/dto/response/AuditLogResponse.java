package com.teb.practice.dto.response;

import com.teb.practice.entity.AuditEventType;

import java.time.LocalDateTime;

public record AuditLogResponse(
        AuditEventType auditEventType,
        String message,
        String ipAddress,
        String userAgent,
        LocalDateTime createdAt) {}
