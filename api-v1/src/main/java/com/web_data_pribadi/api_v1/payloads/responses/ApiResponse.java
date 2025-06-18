package com.web_data_pribadi.api_v1.payloads.responses;

import java.util.Objects;

public record ApiResponse(
    int status,
    boolean success,
    String message,
    String timestamps,
    Object resource) {
  public ApiResponse(
      int status,
      boolean success,
      String message,
      String timestamps,
      Object resource) {
    this.status = Objects.requireNonNull(status, "Status cannot be null.");
    this.success = Objects.requireNonNull(success, "Success cannot be null.");
    this.message = Objects.requireNonNull(message, "Message cannot be null.");
    this.timestamps = Objects.requireNonNull(timestamps, "Timestamps cannot be null.");
    this.resource = Objects.requireNonNull(resource, "Resource cannot be null.");
  }
}
