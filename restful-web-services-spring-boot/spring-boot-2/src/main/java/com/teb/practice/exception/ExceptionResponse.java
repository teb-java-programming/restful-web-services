package com.teb.practice.exception;

import java.util.Date;

public class ExceptionResponse {

    private Date timeStamp;
    private String exceptionMessage;
    private String exceptionDetails;

    /* The exception message format is customised to include specified attributes */
    public ExceptionResponse(Date timeStamp, String exceptionMessage, String exceptionDetails) {
        super();
        this.timeStamp = timeStamp;
        this.exceptionMessage = exceptionMessage;
        this.exceptionDetails = exceptionDetails;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public String getExceptionDetails() {
        return exceptionDetails;
    }
}
