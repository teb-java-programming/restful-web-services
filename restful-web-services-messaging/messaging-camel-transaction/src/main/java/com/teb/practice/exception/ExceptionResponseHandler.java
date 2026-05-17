package com.teb.practice.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.ResponseEntity.status;

import static java.time.LocalDateTime.now;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionResponseHandler {

    @ExceptionHandler(TransactionException.class)
    public ResponseEntity<ErrorResponse> handleInvalidTransaction(TransactionException e) {

        return status(BAD_REQUEST)
                .body(
                        new ErrorResponse(
                                now(), BAD_REQUEST.value(), BAD_REQUEST.name(), e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception e) {

        return status(BAD_REQUEST)
                .body(
                        new ErrorResponse(
                                now(), BAD_REQUEST.value(), BAD_REQUEST.name(), e.getMessage()));
    }
}
