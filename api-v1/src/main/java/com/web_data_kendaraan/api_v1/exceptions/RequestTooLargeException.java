package com.web_data_kendaraan.api_v1.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
public class RequestTooLargeException extends RuntimeException {
  public RequestTooLargeException(String message) {
    super(message);
  }
}
