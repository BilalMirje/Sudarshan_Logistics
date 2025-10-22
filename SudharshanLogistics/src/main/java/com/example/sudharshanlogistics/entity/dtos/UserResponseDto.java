package com.example.sudharshanlogistics.entity.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDto {
        private UUID userId;
        private String name;
        private String address;
        private String username;
        private String password;
        private String contact;
        private String email;
        private String imageUrl;
        private LocalDate birthDate;
        private String panCardNo;
        private String aadharNo;
        private UUID roleId;
        private UUID branchId;
        private String resetToken;

}
