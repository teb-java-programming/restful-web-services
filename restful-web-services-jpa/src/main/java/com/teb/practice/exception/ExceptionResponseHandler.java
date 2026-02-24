package com.teb.practice.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.Date;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class ExceptionResponseHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(Exception.class)
  public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request)
      throws Exception {
    ExceptionResponse exceptionResponse =
        new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));

    return new ResponseEntity<Object>(exceptionResponse, INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(GameNotFoundException.class)
  public final ResponseEntity<Object> handleGameNotFoundExceptions(
      GameNotFoundException ex, WebRequest request) throws Exception {
    ExceptionResponse exceptionResponse =
        new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));

    return new ResponseEntity<Object>(exceptionResponse, NOT_FOUND);
  }

  @ExceptionHandler(DeveloperNotFoundException.class)
  public final ResponseEntity<Object> handleDeveloperNotFoundExceptions(
      DeveloperNotFoundException ex, WebRequest request) throws Exception {
    ExceptionResponse exceptionResponse =
        new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));

    return new ResponseEntity<Object>(exceptionResponse, NOT_FOUND);
  }

  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    ExceptionResponse exceptionResponse =
        new ExceptionResponse(
            new Date(), "Input validation failed.", ex.getBindingResult().toString());

    return new ResponseEntity<Object>(exceptionResponse, BAD_REQUEST);
  }
}
