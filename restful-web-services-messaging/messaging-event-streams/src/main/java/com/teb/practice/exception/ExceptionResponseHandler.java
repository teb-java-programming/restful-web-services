package com.teb.practice.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.ResponseEntity.status;

import static java.time.LocalDateTime.now;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionResponseHandler {

    @ExceptionHandler(RideNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRideNotFound(RideNotFoundException e) {

        return status(NOT_FOUND)
                .body(
                        new ErrorResponse(
                                now(), NOT_FOUND.value(), NOT_FOUND.name(), e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception e) {

        return status(BAD_REQUEST)
                .body(
                        new ErrorResponse(
                                now(), BAD_REQUEST.value(), BAD_REQUEST.name(), e.getMessage()));
    }
}
