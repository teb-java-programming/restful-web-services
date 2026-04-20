package com.teb.practice.entity;

public enum AuditEventType {
    LOGIN_SUCCESS,
    LOGIN_FAILED,
    LOGOUT,
    TOKEN_REFRESH,
    TOKEN_REFRESH_FAILED,
    MFA_REQUIRED,
    MFA_SUCCESS,
    MFA_FAILED,
    SESSION_REVOKED
}
