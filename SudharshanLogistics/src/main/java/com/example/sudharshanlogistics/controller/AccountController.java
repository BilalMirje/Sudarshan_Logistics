package com.example.sudharshanlogistics.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.sudharshanlogistics.entity.dtos.account.AccountRequestDto;
import com.example.sudharshanlogistics.entity.dtos.account.AccountResponseDto;
import com.example.sudharshanlogistics.service.AccountService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/account")
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/create-account")
    public ResponseEntity<?> createAccount(@Valid @RequestBody AccountRequestDto accountRequestDto) {
        try {
            AccountResponseDto createdAccount = accountService.createAccount(accountRequestDto);
            return ResponseEntity.ok(createdAccount);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid value: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/update-account")
    public ResponseEntity<?> updateAccount(@RequestParam UUID id,
            @Valid @RequestBody AccountRequestDto accountRequestDto) {
        try {
            AccountResponseDto updatedAccount = accountService.updateAccount(id, accountRequestDto);
            return ResponseEntity.ok(updatedAccount);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid value: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete-account")
    public ResponseEntity<?> deleteAccount(@RequestParam UUID id) {
        try {
            if (!accountService.existsByAccountId(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found with ID: " + id);
            }
            accountService.deleteAccount(id);
            return ResponseEntity.ok("Account deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/get-all-accounts")
    public ResponseEntity<?> getAllAccounts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size,
            @RequestParam(defaultValue = "accountName") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<AccountResponseDto> accounts = accountService.getAllAccounts(pageable);
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/search-accounts")
    public ResponseEntity<List<AccountResponseDto>> searchAccount(
            @RequestParam(required = false) String accountName,
            @RequestParam(required = false) String accountNo) {

        List<AccountResponseDto> result = accountService.getAccountByNameOrNo(accountName, accountNo);
        return ResponseEntity.ok(result);
    }
}
