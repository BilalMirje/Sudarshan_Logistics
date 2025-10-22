package com.example.sudharshanlogistics.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.sudharshanlogistics.entity.dtos.owner.OwnerRequestDtos;
import com.example.sudharshanlogistics.entity.dtos.owner.OwnerResponceDtos;
import com.example.sudharshanlogistics.service.OwnerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/owners")
@RequiredArgsConstructor
public class OwnerController {

  private final OwnerService ownerService;

  @PostMapping("/create-owner")
  public ResponseEntity<?> createOwner(@RequestBody OwnerRequestDtos ownerRequestDtos) {
    return ResponseEntity.ok(ownerService.createOwner(ownerRequestDtos));
  }

  @GetMapping("/get-owner-by-id")
  public ResponseEntity<?> getOwnerById(@RequestParam UUID ownerId) {
    return ResponseEntity.ok(ownerService.getOwnerById(ownerId));
  }

  @GetMapping("/get-all-owners")
  public ResponseEntity<?> getAllOwners(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "id") String sortBy,
      @RequestParam(defaultValue = "asc") String sortDir) {

    Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
        : Sort.by(sortBy).descending();
    Pageable pageable = PageRequest.of(page, size, sort);
    Page<OwnerResponceDtos> owners = ownerService.getAllOwners(pageable);
    return ResponseEntity.ok(owners);
  }

  @GetMapping("/search-owner")
  public ResponseEntity<?> searchOwner(@RequestParam(required = false) String ownerName,
      @RequestParam(required = false) String email) {
    List<OwnerResponceDtos> owners = ownerService.serachOwners(ownerName, email);
    return owners.isEmpty()
        ? ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You are searching with this code or name is not found")
        : ResponseEntity.ok(owners);
  }

  @GetMapping("/get-owners-by-name")
  public ResponseEntity<?> getOwnersByName(@RequestParam String ownerName) {
    return ResponseEntity.ok(ownerService.getOwnersByName(ownerName));
  }

  @PutMapping("/update-owner")
  public ResponseEntity<?> updateOwner(@RequestParam UUID ownerId,
      @RequestBody OwnerRequestDtos ownerRequestDtos) {
    return ResponseEntity.ok(ownerService.updateOwner(ownerId, ownerRequestDtos));
  }

  @DeleteMapping("/delete-owner")
  public ResponseEntity<?> deleteOwner(@RequestParam UUID ownerId) {
    ownerService.deleteOwner(ownerId);
    return ResponseEntity.status(HttpStatus.OK).body("Owner Deleted Successfully");
  }
}
