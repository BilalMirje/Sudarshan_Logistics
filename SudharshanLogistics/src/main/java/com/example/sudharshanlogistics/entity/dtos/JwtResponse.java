package com.example.sudharshanlogistics.entity.dtos;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
    private UUID userId;
    private String username;
    private Boolean isLoggedIn;
    private String jwtToken;
    private RoleDto role;
    private String imageUrl;
}
