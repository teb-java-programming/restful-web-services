package com.teb.practice.exception;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExceptionResponse {

  private Date timeStamp;
  private String exceptionMessage;
  private String exceptionDetails;
}
