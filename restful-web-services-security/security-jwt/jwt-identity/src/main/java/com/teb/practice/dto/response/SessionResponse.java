package com.teb.practice.dto.response;

import java.time.LocalDateTime;

public record SessionResponse(
        String deviceId, String ipAddress, String userAgent, LocalDateTime lastActiveAt) {}
