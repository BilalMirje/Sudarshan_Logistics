package com.example.sudharshanlogistics.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.sudharshanlogistics.entity.dtos.tax.TaxConfigurationRequestDto;
import com.example.sudharshanlogistics.entity.dtos.tax.TaxConfigurationResponseDto;
import com.example.sudharshanlogistics.service.TaxConfigurationService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/taxes")
public class TaxConfigurationController {

    private final TaxConfigurationService taxConfigurationService;

    @PostMapping("/create-tax")
    public ResponseEntity<?> createOrUpdateTaxConfig(@Valid @RequestBody TaxConfigurationRequestDto dto) {
        try {
            TaxConfigurationResponseDto response = taxConfigurationService.createOrUpdateTaxConfig(dto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/get-latest-taxes")
    public ResponseEntity<?> getLatestTaxConfig() {
        try {
            TaxConfigurationResponseDto response = taxConfigurationService.getLatestTaxConfig();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/get-taxes-by-id")
    public ResponseEntity<?> getTaxConfigById(@RequestParam UUID id) {
        try {
            TaxConfigurationResponseDto response = taxConfigurationService.getTaxConfigById(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/update-taxes-by-id")
    public ResponseEntity<?> updateTaxConfig(@RequestParam UUID id,
            @Valid @RequestBody TaxConfigurationRequestDto dto) {
        try {
            TaxConfigurationResponseDto response = taxConfigurationService.updateTaxConfig(id, dto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete-taxes-by-id")
    public ResponseEntity<?> deleteTaxConfig(@RequestParam UUID id) {
        try {
            taxConfigurationService.deleteTaxConfig(id);
            return ResponseEntity.ok("Tax configuration deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }
}
