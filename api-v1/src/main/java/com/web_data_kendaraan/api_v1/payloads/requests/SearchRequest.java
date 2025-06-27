package com.web_data_kendaraan.api_v1.payloads.requests;

import java.util.Objects;

public record SearchRequest(String query) {
  public SearchRequest(String query) {
    this.query = Objects.requireNonNull(query, "Query cannot be null.");
  }
}
