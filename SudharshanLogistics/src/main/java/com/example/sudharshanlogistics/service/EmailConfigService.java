package com.example.sudharshanlogistics.service;

import com.example.sudharshanlogistics.entity.dtos.emailConfig.EmailConfigRequestDto;
import com.example.sudharshanlogistics.entity.dtos.emailConfig.EmailConfigResponseDto;
import com.example.sudharshanlogistics.entity.EmailConfig;

import java.util.UUID;

public interface EmailConfigService {
    EmailConfigResponseDto saveOrUpdate(EmailConfigRequestDto dto);
    EmailConfig getActiveConfig();
    void deleteById(UUID id);

}