package com.example.sudharshanlogistics.service.Impl;


import com.example.sudharshanlogistics.entity.dtos.emailConfig.EmailConfigRequestDto;
import com.example.sudharshanlogistics.entity.dtos.emailConfig.EmailConfigResponseDto;
import com.example.sudharshanlogistics.entity.EmailConfig;

import com.example.sudharshanlogistics.repository.EmailConfigRepository;
import com.example.sudharshanlogistics.service.EmailConfigService;

import java.util.UUID;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
@Service
@RequiredArgsConstructor
public class EmailConfigServiceImpl implements EmailConfigService {

    private final EmailConfigRepository repository;


    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    @Override
    @Transactional
    public EmailConfigResponseDto saveOrUpdate(EmailConfigRequestDto dto) {

        if (dto.getEmail() == null || dto.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email cannot be empty.");
        }

        String email = dto.getEmail().trim();

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Invalid email format. Please provide a valid email address.");
        }

        if (dto.getAppPassword() == null || dto.getAppPassword().isBlank()) {
            throw new IllegalArgumentException("App password cannot be empty.");
        }


        repository.deactivateAll();


        EmailConfig newConfig = EmailConfig.builder()
                .email(email)
                .appPassword(Base64.getEncoder().encodeToString(dto.getAppPassword().getBytes()))
                .active(true)
                .build();

        EmailConfig saved = repository.save(newConfig);
        return new EmailConfigResponseDto(saved.getId(), saved.getEmail(), saved.isActive());
    }

    @Override
    public EmailConfig getActiveConfig() {
        return repository.findByActiveTrue()
                .orElseThrow(() -> new IllegalStateException("No active email configuration found. Please set up email credentials."));
    }

    @Override
    public void deleteById(UUID id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Email configuration not found with ID: " + id);
        }
        repository.deleteById(id);
    }
}