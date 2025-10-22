package com.example.sudharshanlogistics.service.Impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.example.sudharshanlogistics.entity.AppUser;
import com.example.sudharshanlogistics.entity.dtos.UserResponseDto;
import com.example.sudharshanlogistics.repository.UserRepository;
import com.example.sudharshanlogistics.service.AppUserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppUserServiceImpl implements AppUserService {

  private final BCryptPasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final ImageUploadService imageUploadService;

  public AppUser saveUser(AppUser user, MultipartFile file) {
    if (this.userRepository.existsByUsername(user.getUsername())) {
      throw new RuntimeException("Username already exists");
    } else {
      Map<String, String> imageMap = null;
      try {
        if (file != null && !file.isEmpty()) {
          imageMap = this.imageUploadService.uploadUserImage(file);
        } else {
          ClassPathResource imgFile = new ClassPathResource("static/superadmin.jpg");
          imageMap = this.imageUploadService.uploadUserImage(imgFile.getFile());
        }
      } catch (IOException var6) {
        throw new RuntimeException(var6);
      }

      user.setImagePublicId(imageMap.get("publicId"));
      user.setImageUrl(imageMap.get("url"));
      user.setPassword(passwordEncoder.encode(user.getPassword()));
      AppUser savedUser = this.userRepository.save(user);
      return savedUser;
    }
  }

  public UserResponseDto getAppUser(UUID id) {
    AppUser user = this.getSingleUser(id);
    if (user != null) {
      return MapToDto(user);
    } else {
      return null;
    }
  }

  public AppUser updateAppUser(AppUser user, MultipartFile file) {
    AppUser appUser = this.getSingleUser(user.getId());
    if (appUser == null) {
      return null;
    } else {
      try {
        if (file != null && !file.isEmpty()) {
          this.imageUploadService.deleteImage(appUser.getImagePublicId());
          Map<String, String> imageMap = this.imageUploadService.uploadUserImage(file);
          appUser.setImageUrl((String) imageMap.get("url"));
          appUser.setImagePublicId((String) imageMap.get("publicId"));
        }
      } catch (IOException var6) {
        throw new RuntimeException(var6);
      }

      appUser.setName(user.getName());
      appUser.setContact(user.getContact());
      appUser.setAddress(user.getAddress());
      appUser.setUsername(user.getUsername());
      if (user.getPassword() != null && !user.getPassword().isEmpty()) {
        appUser.setPassword(this.passwordEncoder.encode(user.getPassword()));
      }

      appUser.setEmail(user.getEmail());
      appUser.setBirthDate(user.getBirthDate());
      appUser.setAadharNo(user.getAadharNo());
      appUser.setPanCardNo(user.getPanCardNo());
      appUser.setRole(user.getRole());
      appUser.setBranch(user.getBranch());
      return this.userRepository.save(appUser);
    }
  }

  public boolean deleteUserById(UUID userId) {
    Optional<AppUser> userOpt = this.userRepository.findById(userId);
    if (userOpt.isEmpty()) {
      return false;
    } else {
      AppUser user = userOpt.get();
      if (!user.getUsername().equals("superadmin.com")
          && !user.getRole().getRoleName().toLowerCase().equals("manager")) {
        try {
          this.imageUploadService.deleteImage(user.getImagePublicId());
        } catch (Exception var4) {
          throw new RuntimeException("User Image delete failed");
        }

        this.userRepository.deleteById(userId);
        return true;
      } else {
        return false;
      }
    }
  }

  private AppUser getSingleUser(UUID id) {
    Optional<AppUser> user = this.userRepository.findById(id);
    return user.orElse(null);
  }

  @Override
  public Page<UserResponseDto> getAllUsers(Pageable pageable) {
    Page<AppUser> users = this.userRepository.findAll(pageable);
    return users.map(this::MapToDto);
  }

  private UserResponseDto MapToDto(AppUser user) {
    UserResponseDto.UserResponseDtoBuilder builder = UserResponseDto.builder()
        .userId(user.getId())
        .name(user.getName())
        .address(user.getAddress())
        .username(user.getUsername())
        .email(user.getEmail())
        .password(user.getPassword())
        .contact(user.getContact())
        .imageUrl(user.getImageUrl())
        .birthDate(user.getBirthDate())
        .aadharNo(user.getAadharNo())
        .panCardNo(user.getPanCardNo())
        .roleId(user.getRole().getId())
        .resetToken(user.getResetToken());
    if (user.getBranch() != null) {
      builder.branchId(user.getBranch().getId());
    }
    return builder.build();
  }

  @Override
  public List<UserResponseDto> searchUser(String name, String username, String email) {
    List<AppUser> users = userRepository
        .findByNameContainingIgnoreCaseOrUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(name, username, email);
    return users.stream().map(this::MapToDto).toList();
  }

  @Override
  public String generateResetToken(String username) {
    try {
      List<AppUser> allUsers = userRepository.findAll();
      System.err.println("All usernames in DB: " +
          allUsers.stream().map(AppUser::getUsername).toList());
      Optional<AppUser> userOpt = userRepository.findByUsernameIgnoreCase(username);
      if (userOpt.isEmpty()) {
        throw new RuntimeException("User not found with username: " + username);
      }
      AppUser user = userOpt.get();
      String token = UUID.randomUUID().toString();
      user.setResetToken(token);
      user.setResetTokenExpiry(LocalDateTime.now().plusMinutes(15));
      userRepository.save(user);
      return token; // For testing, return token; in real, send email
    } catch (Exception e) {
      System.err.println("Error generating reset token for username " + username +
          ": " + e.getMessage());
      throw e;
    }
  }

  @Override
  public boolean validateResetToken(String token) {
    Optional<AppUser> userOpt = userRepository.findByResetToken(token);
    if (userOpt.isEmpty()) {
      return false;
    }

    AppUser user = userOpt.get();
    LocalDateTime expiry = user.getResetTokenExpiry();
    return expiry != null && expiry.isAfter(LocalDateTime.now());
  }

  @Override
  public boolean resetPassword(String token, String newPassword) {
    Optional<AppUser> userOpt = userRepository.findByResetToken(token);
    if (userOpt.isEmpty()) {
      throw new RuntimeException("Invalid token");
    }

    AppUser user = userOpt.get();
    if (user.getResetTokenExpiry() == null || user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
      throw new RuntimeException("Token expired");
    }

    user.setPassword(passwordEncoder.encode(newPassword));
    user.setResetToken(null);
    user.setResetTokenExpiry(null);
    userRepository.save(user);

    return true;
  }

  @Override
  public String getResetToken(String username) {
    Optional<AppUser> userOpt = userRepository.findByUsernameIgnoreCase(username);
    if (userOpt.isEmpty()) {
      throw new RuntimeException("User not found with username: " + username);
    }

    AppUser user = userOpt.get();
    if (user.getResetToken() == null) {
      throw new RuntimeException("Reset token not generated for this user");
    }

    if (user.getResetTokenExpiry() == null || user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
      throw new RuntimeException("Reset token expired");
    }

    return user.getResetToken();
  }

}
