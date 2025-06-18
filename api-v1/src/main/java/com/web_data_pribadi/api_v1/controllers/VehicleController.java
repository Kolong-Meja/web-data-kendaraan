package com.web_data_pribadi.api_v1.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.web_data_pribadi.api_v1.interfaces.IVehicle;
import com.web_data_pribadi.api_v1.payloads.requests.CreateVehicleRequest;
import com.web_data_pribadi.api_v1.payloads.requests.UpdateVehicleRequest;
import com.web_data_pribadi.api_v1.payloads.responses.ApiResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/vehicles")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", allowCredentials = "true", methods = {
    RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE })
public class VehicleController {
  private final IVehicle iVehicle;

  public VehicleController(IVehicle iVehicle) {
    this.iVehicle = iVehicle;
  }

  @GetMapping
  public ResponseEntity<ApiResponse> findAll() {
    return iVehicle.findAll();
  }

  @GetMapping("/{nrk}")
  public ResponseEntity<ApiResponse> findOne(@PathVariable String nrk) {
    return iVehicle.findOne(nrk);
  }

  @PostMapping
  public ResponseEntity<ApiResponse> save(
      @Valid @RequestBody(required = true) CreateVehicleRequest createVehicleRequest) {
    return iVehicle.save(createVehicleRequest);
  }

  @PutMapping("/{nrk}")
  public ResponseEntity<ApiResponse> update(@PathVariable String nrk,
      @Valid @RequestBody(required = false) UpdateVehicleRequest updateVehicleRequest) {
    return iVehicle.update(nrk, updateVehicleRequest);
  }

  @DeleteMapping("/{nrk}")
  public ResponseEntity<ApiResponse> delete(@PathVariable String nrk) {
    return iVehicle.delete(nrk);
  }
}
