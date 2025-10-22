package com.example.sudharshanlogistics.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table
public class AppUser extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    private String name;
    private String address;
    private String username;
    private String password;
    private String contact;
    private String email;
    private LocalDate birthDate;
    private String panCardNo;
    private String aadharNo;
    private String imageUrl;
    private String imagePublicId;
    private String resetToken;
    private LocalDateTime resetTokenExpiry;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToOne
    private Branch branch;
}
