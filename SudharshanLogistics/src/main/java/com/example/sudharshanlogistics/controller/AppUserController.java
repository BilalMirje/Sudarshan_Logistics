package com.example.sudharshanlogistics.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.sudharshanlogistics.entity.AppUser;
import com.example.sudharshanlogistics.entity.Role;
import com.example.sudharshanlogistics.entity.dtos.RoleDto;
import com.example.sudharshanlogistics.entity.dtos.UserResponseDto;
import com.example.sudharshanlogistics.repository.BranchRepository;
import com.example.sudharshanlogistics.repository.RoleRepository;
import com.example.sudharshanlogistics.service.AppUserService;
import com.example.sudharshanlogistics.service.RoleService;
import com.example.sudharshanlogistics.config.IpAddressUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Tag(name = "App-User-Controller", description = "You can create,read,delete,update the user")
public class AppUserController {

  private final BCryptPasswordEncoder passwordEncoder;
  private final AppUserService appUserService;
  private final RoleService roleService;
  private final RoleRepository roleRepository;
  private final BranchRepository branchRepository;

  @PostMapping("/create-user")
  @Operation(summary = "create-user", description = "create user with given fields")
  public ResponseEntity<?> createUser(
      @RequestParam String name,
      @RequestParam String address,
      @RequestParam String username,
      @RequestParam String password,
      @RequestParam String contact,
      @RequestParam String email,
      @RequestParam(required = false) String birthDate,
      @RequestParam String aadharNo,
      @RequestParam String panCardNo,
      @RequestParam(name = "image", required = false) MultipartFile imageFile,
      @RequestParam UUID role_id,
      @RequestParam UUID branch_id)
      throws IOException {
    AppUser user = new AppUser();
    user.setName(name);
    user.setAddress(address);
    user.setUsername(username);
    user.setPassword(this.passwordEncoder.encode(password));
    user.setContact(contact);
    user.setEmail(email);
    if (birthDate != null && !birthDate.isEmpty()) {
      user.setBirthDate(LocalDate.parse(birthDate));
    }
    user.setAadharNo(aadharNo);
    user.setPanCardNo(panCardNo);
    user.setRole(this.roleRepository.findById(role_id).get());
    user.setBranch(this.branchRepository.findById(branch_id).orElse(null));
    AppUser appUser = this.appUserService.saveUser(user, imageFile);
    return appUser != null ? ResponseEntity.ok(appUser) : ResponseEntity.ok((Object) null);
  }

  @GetMapping("/get-user")
  @Operation(summary = "get-user", description = "get user with given id")
  public ResponseEntity<?> getUser(@RequestParam UUID id) {
    return ResponseEntity.ok(this.appUserService.getAppUser(id));
  }

  @GetMapping("/get-all-users")
  @Operation(summary = "get-all-users", description = "get all users")
  public ResponseEntity<?> getAllUsers(@RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "id") String sortBy,
      @RequestParam(defaultValue = "asc") String sortDir) {

    Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
        : Sort.by(sortBy).descending();
    Pageable pageable = PageRequest.of(page, size, sort);
    Page<UserResponseDto> responce = appUserService.getAllUsers(pageable);
    return ResponseEntity.ok(responce);

  }

  @GetMapping("search-user")
  @Operation(summary = "searching-all-users", description = "get all users with end points")
  public ResponseEntity<?> searchUsers(@RequestParam(required = false) String name,
      @RequestParam(required = false) String username, @RequestParam(required = false) String email) {
    List<UserResponseDto> users = appUserService.searchUser(name, username, email);
    return users.isEmpty()
        ? ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("You are searching with name or username or email is not found")
        : ResponseEntity.ok(users);
  }

  @PutMapping("/update-user")
  @Operation(summary = "update user", description = "update user with given id")
  public ResponseEntity<?> updateUser(@RequestParam UUID id,
      @RequestParam String name,
      @RequestParam String address,
      @RequestParam String username,
      @RequestParam(required = false) String password,
      @RequestParam String contact,
      @RequestParam String email,
      @RequestParam(required = false) String birthDate,
      @RequestParam String aadharNo,
      @RequestParam String panCardNo,
      @RequestParam(name = "file", required = false) MultipartFile imageFile,
      @RequestParam UUID role_id,
      @RequestParam UUID branch_id)
      throws IOException {
    AppUser appUser = new AppUser();
    appUser.setId(id);
    appUser.setName(name);
    appUser.setAddress(address);
    appUser.setUsername(username);
    appUser.setPassword(password);
    appUser.setContact(contact);
    appUser.setEmail(email);
    if (birthDate != null && !birthDate.isEmpty()) {
      appUser.setBirthDate(LocalDate.parse(birthDate));
    }
    appUser.setAadharNo(aadharNo);
    appUser.setPanCardNo(panCardNo);
    appUser.setRole(this.roleRepository.findById(role_id).get());
    appUser.setBranch(this.branchRepository.findById(branch_id).orElse(null));
    AppUser user = this.appUserService.updateAppUser(appUser, imageFile);
    return user != null ? ResponseEntity.ok(user) : ResponseEntity.ok((Object) null);
  }

  @DeleteMapping("/delete-user")
  @Operation(summary = "delete user", description = "delete user with given id")
  public ResponseEntity<?> deleteUser(@RequestParam UUID id) {
    boolean result = this.appUserService.deleteUserById(id);
    return result ? ResponseEntity.ok(true) : ResponseEntity.status(HttpStatus.CONFLICT).build();
  }

  @PostMapping("/create-role")
  @Operation(summary = "create role", description = "create role with given fields")
  public ResponseEntity<?> createRole(@RequestBody RoleDto roleDto, HttpServletRequest request) {
    String clientIpAddress = IpAddressUtil.getClientIpAddress(request);
    Role role = this.roleService.createRole(roleDto, clientIpAddress);
    return role != null ? ResponseEntity.ok(role) : ResponseEntity.ok((Object) null);
  }

  @GetMapping("/get-all-roles")
  @Operation(summary = "get all roles", description = "get all roles from role entity")
  public ResponseEntity<?> getAllRole(@RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "roleName") String sortBy,
      @RequestParam(defaultValue = "asc") String sortDir) {

    Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
        : Sort.by(sortBy).descending();
    Pageable pageable = PageRequest.of(page, size, sort);
    Page<RoleDto> roles = roleService.getAllRoles(pageable);
    return ResponseEntity.ok(roles);
  }

  @GetMapping("/search-role")
  @Operation(summary = "search roles", description = "search roles by name")
  public ResponseEntity<?> searchRoles(@RequestParam String roleName) {
    List<RoleDto> roles = roleService.searchRoles(roleName);
    return ResponseEntity.ok(roles);
  }

  @GetMapping("/get-role-by-role-name")
  @Operation(summary = "get role by name", description = "get role by name from given name")
  public ResponseEntity<?> getRole(@RequestParam String roleName) {
    try {
      return ResponseEntity.status(HttpStatus.OK).body(this.roleService.getRoleByRoleName(roleName));
    } catch (Exception var3) {
      return ResponseEntity.status(HttpStatus.OK).body((Object) null);
    }
  }

  @PutMapping("/update-role")
  @Operation(summary = "update roles", description = "update role from given id")
  public ResponseEntity<?> updateRole(@RequestBody RoleDto roleDto) {
    return ResponseEntity.status(HttpStatus.OK).body(this.roleService.updateRole(roleDto));
  }

  @DeleteMapping("/delete-role")
  @Operation(summary = "delete-role", description = "delete role by given id")
  public ResponseEntity<?> deleteRole(@RequestParam UUID id) {
    boolean result = roleService.deleteRole(id);
    return result ? ResponseEntity.ok(true)
        : ResponseEntity.status(HttpStatus.CONFLICT).body("Role is assigned or role is not found");
  }

}
