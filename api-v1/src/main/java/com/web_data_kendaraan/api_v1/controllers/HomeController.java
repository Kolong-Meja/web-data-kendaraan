package com.web_data_kendaraan.api_v1.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HomeController {
  @GetMapping
  public ResponseEntity<Map<String, Object>> home() {
    Map<String, Object> response = new HashMap<>();
    response.put("status", "ok");
    response.put("message", "API is running");
    response.put("version", "1.0");
    response.put("timestamp", new Date());
    return ResponseEntity.ok(response);
  }
}
