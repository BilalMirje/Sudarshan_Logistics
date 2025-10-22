package com.example.sudharshanlogistics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
@RestController
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class SudharshanLogisticsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SudharshanLogisticsApplication.class, args);
    }

    @GetMapping("/test")
    public ResponseEntity<?> testEndPoint() {
        return ResponseEntity.ok("This is a test endpoint of this project");
    }

}
