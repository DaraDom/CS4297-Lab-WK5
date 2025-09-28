package com.example.async_microservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExportController {

    @GetMapping("/export")
    public ResponseEntity<String> exportData() throws InterruptedException{
        Thread.sleep(10000);
        return ResponseEntity.ok("Export completed");
    }
}
