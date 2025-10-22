package com.example.sudharshanlogistics.controller;

import com.example.sudharshanlogistics.config.jwt.JwtUtils;
import com.example.sudharshanlogistics.entity.AppUser;
import com.example.sudharshanlogistics.entity.dtos.JwtRequest;
import com.example.sudharshanlogistics.entity.dtos.JwtResponse;
import com.example.sudharshanlogistics.entity.dtos.PermissionDto;
import com.example.sudharshanlogistics.entity.dtos.RoleDto;
import com.example.sudharshanlogistics.entity.Permissions;
import com.example.sudharshanlogistics.entity.Privilege;
import com.example.sudharshanlogistics.entity.Role;
import com.example.sudharshanlogistics.repository.PermissionsRepository;
import com.example.sudharshanlogistics.repository.RoleRepository;
import com.example.sudharshanlogistics.repository.UserRepository;
import com.example.sudharshanlogistics.service.Impl.ImageUploadService;
import com.example.sudharshanlogistics.service.jwt.MyUserDetailsService;
import com.example.sudharshanlogistics.service.AppUserService;

import io.swagger.v3.oas.annotations.tags.Tag;

import com.example.sudharshanlogistics.service.RoleService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
@RequestMapping("/api/service")
@Tag(name = "JWT-Authentication-Controller")

public class JwtAuthenticationController {

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionsRepository permissionRepository;
    private final RoleService roleService;
    private final AuthenticationManager authenticationManager;
    private final MyUserDetailsService userDetailsService;
    private final JwtUtils jwtUtils;
    private final ImageUploadService imageUploadService;
    private final AppUserService appUserService;

    @PostConstruct
    public void createAdmin() throws IOException {
        Optional<AppUser> optionalUser = userRepository.findByUsername("superadmin.com");
        if (optionalUser.isEmpty()) {

            // Optional<Role> optionalRole = roleRepository.findByRoleName("SUPER_ADMIN");

            Role savedRole = roleRepository.findByRoleName("SUPER_ADMIN")
                    .orElseGet(() -> {
                        Role role = new Role();
                        role.setRoleName("SUPER_ADMIN");
                        role.setRoleDescription("This is super admin role");
                        return roleRepository.save(role);
                    });

            if (permissionRepository.getPermissionsByRole(savedRole).isEmpty()) {
                Privilege privilege = new Privilege();
                privilege.setWritePermission("WRITE");
                privilege.setReadPermission("READ");
                privilege.setDeletePermission("DELETE");
                privilege.setUpdatePermission("UPDATE");

                Permissions permissions = new Permissions();
                permissions.setUserPermission("ALL_PERMISSIONS");
                permissions.setRole(savedRole);
                permissions.setPrivilege(privilege);
                roleService.createPermissions(List.of(permissions));
            }

            AppUser user = new AppUser();
            user.setName("Super Admin");
            user.setUsername("superadmin.com");
            user.setContact("1234567890");
            user.setAddress("");
            user.setEmail("-");
            user.setBirthDate(null);
            user.setPanCardNo(null);
            user.setAadharNo(null);
            user.setRole(savedRole);
            user.setPassword(passwordEncoder.encode("superadmin@123"));

            ClassPathResource imgFile = new ClassPathResource("static/superadmin.jpg");
            Map<String, String> imageMap = imageUploadService.uploadUserImage(imgFile.getFile());

            user.setImageUrl(imageMap.get("url"));
            user.setImagePublicId(imageMap.get("publicId"));
            userRepository.save(user);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody JwtRequest jwtRequest) {
        System.out.println(jwtRequest.getUsername() + "--->" + jwtRequest.getPassword());
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(), jwtRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Incorrect Username or Password.");
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtRequest.getUsername());
        System.out.println("DB password: " + userDetails.getPassword());
        System.out.println("Raw password: " + jwtRequest.getPassword());
        Optional<AppUser> optionalUser = userRepository.findByUsername(userDetails.getUsername());
        String token = jwtUtils.generateToken(userDetails.getUsername());
        JwtResponse response = new JwtResponse();

        if (optionalUser.isPresent()) {
            // optionalUser.get().setIsUserLoggedIn(true);
            optionalUser.get().setUpdatedAt(LocalDateTime.now());
            userRepository.save(optionalUser.get());

            response.setIsLoggedIn(true);
            response.setJwtToken(token);
            Role role = optionalUser.get().getRole();

            RoleDto roleDto = new RoleDto();

            roleDto.setRoleId(role.getId());
            roleDto.setRoleName(role.getRoleName());
            roleDto.setRoleDescription(role.getRoleDescription());

            List<PermissionDto> permissionDtos = new ArrayList<>();
            for (Permissions permission : role.getPermissions()) {
                PermissionDto permissionDto = new PermissionDto();
                List<String> privileges = new ArrayList<>();
                permissionDto.setUserPermission(permission.getUserPermission());
                privileges.add(permission.getPrivilege().getReadPermission());
                privileges.add(permission.getPrivilege().getWritePermission());
                privileges.add(permission.getPrivilege().getUpdatePermission());
                privileges.add(permission.getPrivilege().getDeletePermission());

                permissionDto.setPrivileges(privileges);
                permissionDtos.add(permissionDto);
            }

            roleDto.setPermissions(permissionDtos);

            response.setRole(roleDto);
            AppUser user = optionalUser.get();
            response.setUserId(user.getId());
            response.setUsername(user.getUsername());
            response.setImageUrl(user.getImageUrl());

            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        return null;
    }

    @PostMapping("/is-token-expired")
    public ResponseEntity<?> isTokenExpired(@RequestBody JwtResponse jwtResponse) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(jwtUtils.isTokenExpired(jwtResponse.getJwtToken()));
        } catch (Exception e) {
            Optional<AppUser> user = userRepository.findByUsername(jwtResponse.getUsername());
            if (user.isPresent()) {
                user.get().setUpdatedAt(LocalDateTime.now());
                // user.get().setIsUserLoggedIn(false);
                AppUser save = userRepository.save(user.get());
                return ResponseEntity.ok(save);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(true);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@RequestParam String username) {
        Optional<AppUser> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            optionalUser.get().setUpdatedAt(LocalDateTime.now());
            // optionalUser.get().setIsUserLoggedIn(false);
            AppUser save = userRepository.save(optionalUser.get());
            return ResponseEntity.ok(save);
        }
        return ResponseEntity.ok(false);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String username) {
        try {
            String token = appUserService.generateResetToken(username);
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Your reset token is not generated");
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        boolean success = appUserService.resetPassword(token, newPassword);
        if (success) {
            return ResponseEntity.ok("Password reset successfully");
        } else {
            return ResponseEntity.badRequest().body("Invalid or expired token");
        }
    }

    @GetMapping("/get-reset-token")
    public ResponseEntity<?> getResetToken(@RequestParam String username) {
        return ResponseEntity.ok(this.appUserService.getResetToken(username));
    }

    @GetMapping("/get-all-usernames")
    public ResponseEntity<?> getAllUsernames() {
        List<String> usernames = userRepository.findAll().stream().map(AppUser::getUsername).toList();
        return ResponseEntity.ok(usernames);
    }

}
