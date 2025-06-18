package com.web_data_pribadi.api_v1.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.web_data_pribadi.api_v1.payloads.requests.CreateVehicleRequest;
import com.web_data_pribadi.api_v1.payloads.requests.SearchRequest;
import com.web_data_pribadi.api_v1.payloads.requests.UpdateVehicleRequest;
import com.web_data_pribadi.api_v1.payloads.responses.ApiResponse;

@Component
public interface IVehicle {
  ResponseEntity<ApiResponse> findAll(SearchRequest searchRequest);

  ResponseEntity<ApiResponse> findOne(String nrk);

  ResponseEntity<ApiResponse> save(CreateVehicleRequest createVehicleRequest);

  ResponseEntity<ApiResponse> update(String nrk, UpdateVehicleRequest updateVehicleRequest);

  ResponseEntity<ApiResponse> delete(String nrk);
}
