package com.teb.practice.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class ExceptionResponse {

    private Date timeStamp;
    private String exceptionMessage;
    private String exceptionDetails;
}
