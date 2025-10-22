package com.example.sudharshanlogistics.entity.dtos.emailConfig;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class EmailConfigResponseDto {
    private UUID id;
    private String email;
    private boolean active;
}

