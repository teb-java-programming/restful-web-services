package com.teb.practice.exception;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = NOT_FOUND)
public class DeveloperNotFoundException extends RuntimeException {

  private static final long serialVersionUID = -2346471898736960202L;

  public DeveloperNotFoundException(String message) {
    super(message.concat(", was not found."));
  }
}
