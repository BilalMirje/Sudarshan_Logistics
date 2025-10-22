package com.example.sudharshanlogistics.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.example.sudharshanlogistics.entity.AppUser;
import com.example.sudharshanlogistics.entity.dtos.UserResponseDto;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface AppUserService {

  AppUser saveUser(AppUser user, MultipartFile imageFile) throws IOException;

  UserResponseDto getAppUser(UUID id);

  AppUser updateAppUser(AppUser user, MultipartFile imageFile) throws IOException;

  boolean deleteUserById(UUID userId);

  Page<UserResponseDto> getAllUsers(Pageable pageable);

  List<UserResponseDto> searchUser(String name, String username, String email);

  String generateResetToken(String email);

  boolean validateResetToken(String token);

  boolean resetPassword(String token, String newPassword);

  String getResetToken(String username);

}
