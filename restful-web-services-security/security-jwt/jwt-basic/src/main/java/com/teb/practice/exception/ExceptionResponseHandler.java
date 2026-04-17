package com.teb.practice.exception;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import com.teb.practice.response.ApiResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionResponseHandler {

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(FORBIDDEN)
    public ApiResponse<String> handleAccessDenied(AccessDeniedException e) {

        return new ApiResponse<>("Forbidden");
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(UNAUTHORIZED)
    public ApiResponse<String> handleAuthException(AuthenticationException e) {

        return new ApiResponse<>("Unauthorized");
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    @ResponseStatus(UNAUTHORIZED)
    public ApiResponse<String> handleInvalidCredentials(InvalidCredentialsException e) {

        return new ApiResponse<>(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ApiResponse<String> handleGeneric(Exception e) {

        return new ApiResponse<>("Unexpected error");
    }
}
