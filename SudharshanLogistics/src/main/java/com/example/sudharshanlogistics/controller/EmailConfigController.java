package com.example.sudharshanlogistics.controller;

import com.example.sudharshanlogistics.entity.dtos.emailConfig.EmailConfigRequestDto;
import com.example.sudharshanlogistics.entity.dtos.emailConfig.EmailConfigResponseDto;
import com.example.sudharshanlogistics.service.EmailConfigService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/email-config")
@RequiredArgsConstructor
public class EmailConfigController {

    private final EmailConfigService service;

    @PostMapping("/save-email")
    public ResponseEntity<?> saveOrUpdate(@Valid @RequestBody EmailConfigRequestDto dto) {
        try {
            EmailConfigResponseDto response = service.saveOrUpdate(dto);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid input: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error saving email config: " + e.getMessage());
        }
    }

    @GetMapping("/get-active-email")
    public ResponseEntity<?> getActive() {
        try {
            var config = service.getActiveConfig();
            EmailConfigResponseDto response = new EmailConfigResponseDto(
                    config.getId(), config.getEmail(), config.isActive()
            );
            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete-email-by-id")
    public ResponseEntity<?> deleteConfig(@RequestParam UUID id) {
        try {
            service.deleteById(id);
            return ResponseEntity.ok("Email configuration deleted successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting email configuration: " + e.getMessage());
        }
    }
}
