package com.web_data_pribadi.api_v1.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web_data_pribadi.api_v1.dtos.VehicleDTO;
import com.web_data_pribadi.api_v1.interfaces.IVehicle;
import com.web_data_pribadi.api_v1.payloads.requests.SearchRequest;
import com.web_data_pribadi.api_v1.payloads.responses.ApiResponse;

@WebMvcTest(VehicleController.class)
public class VehicleControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private IVehicle iVehicle;

  private String timestamp;
  private ApiResponse apiResponse;

  @BeforeEach
  void setup() {
    var now = LocalDateTime.now(ZoneId.of("Asia/Jakarta"));
    var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    timestamp = now.format(formatter);

    apiResponse = new ApiResponse(
        HttpStatus.OK.value(),
        true,
        "Success",
        timestamp,
        new HashMap<>());
  }

  @Test
  void testFindAll() throws Exception {
    SearchRequest searchRequest = new SearchRequest("test");

    VehicleDTO vehicle1 = new VehicleDTO("B-1234-XY", "John Doe", "Jakarta", "Honda", 2020, 150, "Black", "Bensin");
    VehicleDTO vehicle2 = new VehicleDTO("B-5678-AB", "Jane Smith", "Bandung", "Yamaha", 2019, 125, "Red", "Bensin");

    ApiResponse mockResponse = new ApiResponse(
        HttpStatus.OK.value(),
        true,
        "Successfully fetch vehicles ✅",
        timestamp,
        Arrays.asList(vehicle1, vehicle2));

    when(iVehicle.findAll(any(SearchRequest.class))).thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

    mockMvc.perform(get("/v1/vehicles").param("query", "test"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").value("Successfully fetch vehicles ✅"))
        .andExpect(jsonPath("$.data").isArray())
        .andExpect(jsonPath("$.data[0].nrk").value("B-1234-XY"))
        .andExpect(jsonPath("$.data[1].nrk").value("B-5678-AB"));

    verify(iVehicle, times(1)).findAll(any(SearchRequest.class));
  }
}
