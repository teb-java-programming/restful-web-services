package com.teb.practice.exception;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = NOT_FOUND)
public class GameNotFoundException extends RuntimeException {

  private static final long serialVersionUID = -3629865774018477189L;

  public GameNotFoundException(String message) {
    super(message.concat(", was not found."));
  }
}
