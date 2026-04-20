package com.teb.practice.controller;

import static org.springframework.http.ResponseEntity.ok;

import com.teb.practice.dto.response.AuditLogResponse;
import com.teb.practice.dto.response.SessionResponse;
import com.teb.practice.repository.UserRepository;
import com.teb.practice.service.audit.AuditLogService;
import com.teb.practice.service.session.SessionService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final SessionService sessionService;
    private final UserRepository userRepository;
    private final AuditLogService auditLogService;

    @GetMapping("/current")
    public String currentUser(Authentication authentication) {

        return "Logged in as: " + authentication.getName();
    }

    @GetMapping("/sessions")
    public ResponseEntity<List<SessionResponse>> sessions(Authentication authentication) {

        return ok(
                sessionService.getAllSessionsByEmail(authentication.getName()).stream()
                        .map(
                                session ->
                                        new SessionResponse(
                                                session.getDeviceId(),
                                                session.getIpAddress(),
                                                session.getUserAgent(),
                                                session.getLastActiveAt()))
                        .toList());
    }

    @DeleteMapping("/sessions")
    public ResponseEntity<Void> revokeSession(
            @RequestParam String deviceId, Authentication authentication) {

        sessionService.deleteSessionByDevice(
                userRepository.findByEmail(authentication.getName()).orElseThrow().getId(),
                deviceId);

        return ok().build();
    }

    @GetMapping("/audit-logs")
    public ResponseEntity<List<AuditLogResponse>> getAuditLogs(Authentication authentication) {

        return ok(
                auditLogService
                        .getLogsByUserId(
                                userRepository
                                        .findByEmail(authentication.getName())
                                        .orElseThrow()
                                        .getId())
                        .stream()
                        .map(
                                log ->
                                        new AuditLogResponse(
                                                log.getEventType(),
                                                log.getMessage(),
                                                log.getIpAddress(),
                                                log.getUserAgent(),
                                                log.getCreatedAt()))
                        .toList());
    }
}
