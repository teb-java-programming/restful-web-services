package com.teb.practice.exception;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.ResponseEntity.status;

import static java.time.LocalDateTime.now;
import static java.util.Map.of;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionResponseHandler {

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<?> handleAuth(AuthException e) {

        return status(UNAUTHORIZED)
                .body(
                        of(
                                "error", e.getMessage(),
                                "timestamp", now()));
    }

    @ExceptionHandler(MfaException.class)
    public ResponseEntity<?> handleMfa(MfaException e) {

        return status(FORBIDDEN)
                .body(
                        of(
                                "error", e.getMessage(),
                                "timestamp", now()));
    }

    @ExceptionHandler(SessionException.class)
    public ResponseEntity<?> handleSession(SessionException e) {

        return status(NOT_FOUND)
                .body(
                        of(
                                "error", e.getMessage(),
                                "timestamp", now()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneric(Exception e) {

        return status(FORBIDDEN)
                .body(
                        of(
                                "error", e.getMessage(),
                                "timestamp", now()));
    }
}
