package com.teb.practice.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import static java.time.LocalDateTime.now;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class ExceptionResponseHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(
            ResourceNotFoundException e, WebRequest webRequest) {

        return new ResponseEntity<>(
                new ErrorResponse(
                        now(),
                        NOT_FOUND.value(),
                        NOT_FOUND.getReasonPhrase(),
                        e.getMessage(),
                        webRequest.getDescription(false).replace("uri=", "")),
                NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException e, WebRequest webRequest) {

        return new ResponseEntity<>(
                new ErrorResponse(
                        now(),
                        BAD_REQUEST.value(),
                        BAD_REQUEST.getReasonPhrase(),
                        e.getMessage(),
                        webRequest.getDescription(false).replace("uri=", "")),
                BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception e, WebRequest webRequest) {

        return new ResponseEntity<>(
                new ErrorResponse(
                        now(),
                        INTERNAL_SERVER_ERROR.value(),
                        INTERNAL_SERVER_ERROR.getReasonPhrase(),
                        e.getMessage(),
                        webRequest.getDescription(false).replace("uri=", "")),
                INTERNAL_SERVER_ERROR);
    }
}
