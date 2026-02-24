package com.teb.practice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class BookNotFoundException extends RuntimeException {

    @Serial private static final long serialVersionUID = -3629865774018477189L;

    public BookNotFoundException(String message) {

        super(message.concat(", was not found."));
    }
}
