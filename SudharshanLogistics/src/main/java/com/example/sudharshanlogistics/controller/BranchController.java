package com.example.sudharshanlogistics.controller;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.sudharshanlogistics.entity.dtos.branch.BranchRequestDto;
import com.example.sudharshanlogistics.entity.dtos.branch.BranchResponceDto;
import com.example.sudharshanlogistics.service.BranchService;
import org.springframework.data.domain.Sort;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/branch")
@RequiredArgsConstructor
@Tag(name = "Branch Controller", description = "CRUD operations for Branch entity")
@CrossOrigin("*")
public class BranchController {

  private final BranchService branchService;

  @PostMapping("/create-branch")
  @Operation(summary = "Create Branch", description = "Create a new branch")
  public ResponseEntity<?> createBranch(@RequestBody BranchRequestDto branchRequestDto) {
    try {
      BranchResponceDto responseDto = branchService.createBranch(branchRequestDto);
      return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body("This branch code is already in use");
    }
  }

  @GetMapping("/get-branch")
  @Operation(summary = "Get Branch by ID", description = "Get branch details by ID")
  public ResponseEntity<?> getBranchById(@RequestParam UUID id) {
    try {
      BranchResponceDto responseDto = branchService.getBranchById(id);
      return ResponseEntity.ok(responseDto);
    } catch (EntityNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }

  @GetMapping("/get-all-branch")
  @Operation(summary = "Get All Branches", description = "Get paginated & sorted list of all branches")
  public ResponseEntity<?> getAllBranches(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "branchCode") String sortBy,
      @RequestParam(defaultValue = "asc") String sortDir) {

    Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
        : Sort.by(sortBy).descending();
    Pageable pageable = PageRequest.of(page, size, sort);
    Page<BranchResponceDto> branches = branchService.getAllBranches(pageable);
    return ResponseEntity.ok(branches);
  }

  @PutMapping("/update-branch")
  @Operation(summary = "Update Branch", description = "Update branch details by ID")
  public ResponseEntity<?> updateBranch(@RequestParam UUID id, @RequestBody BranchRequestDto branchRequestDto) {
    try {
      BranchResponceDto responseDto = branchService.updateBranch(id, branchRequestDto);
      return ResponseEntity.ok(responseDto);
    } catch (EntityNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body("This branch code is already in use");
    }
  }

  @GetMapping("/search-branch")
  @Operation(summary = "Search Branches", description = "Search branches by name")
  public ResponseEntity<?> searchBranches(@RequestParam(required = false) String name,
      @RequestParam(required = false) String branchCode) {
    List<BranchResponceDto> branches = branchService.searchBranches(name, branchCode);
    return branches.isEmpty()
        ? ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You are searching with this code or name is not found")
        : ResponseEntity.ok(branches);
  }

  @DeleteMapping("/delete-branch")
  @Operation(summary = "Delete Branch", description = "Delete branch by ID")
  public ResponseEntity<?> deleteBranch(@RequestParam UUID id) {
    try {
      branchService.deleteBranch(id);
      return ResponseEntity.ok().build();
    } catch (EntityNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }
}
