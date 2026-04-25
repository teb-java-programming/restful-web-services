package com.teb.practice.exception;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.ResponseEntity.status;

import static java.time.LocalDateTime.now;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionResponseHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(ProductNotFoundException e) {

        return status(NOT_FOUND)
                .body(
                        new ErrorResponse(
                                now(), NOT_FOUND.value(), NOT_FOUND.name(), e.getMessage()));
    }
}
