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

import com.example.sudharshanlogistics.entity.dtos.lorryrInvoice.LorryReceiptRequestDto;
import com.example.sudharshanlogistics.entity.dtos.lorryrInvoice.LorryReceiptResponseDto;
import com.example.sudharshanlogistics.service.LorryInvoiceService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/lorry-receipts")
@RequiredArgsConstructor
public class LorryInvoiceController {

    private final LorryInvoiceService lorryReceiptService;

    @PostMapping("/create-lorry-invoice")
    public ResponseEntity<?> createLorryReceipt(@Valid @RequestBody LorryReceiptRequestDto dto) {
        try {
            LorryReceiptResponseDto created = lorryReceiptService.createLorryReceipt(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/get-lorry-invoice")
    public ResponseEntity<?> getLorryReceiptById(@RequestParam UUID id) {
        try {
            LorryReceiptResponseDto dto = lorryReceiptService.getLorryReceiptById(id);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/get-lorry-invoice-by-wayBillNo")
    public ResponseEntity<?> getLorryReceiptByWayBillNo(@RequestParam String wayBillNo) {
        try {
            LorryReceiptResponseDto dto = lorryReceiptService.getLorryReceiptByWayBillNo(wayBillNo);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/get-all-lorry-invoice")
    public ResponseEntity<?> getAllLorryReceipts(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "wayBillNo") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<LorryReceiptResponseDto> responce = lorryReceiptService.getAllLorryReceipts(pageable);
        return ResponseEntity.ok(responce);
    }

    @PutMapping("/update-lorry-invoice-status-by-id")
    public ResponseEntity<?> updateLorryReceiptStatus(@RequestParam UUID id, @RequestParam String status) {
        try {
            LorryReceiptResponseDto updated = lorryReceiptService.updateLorryReceiptStatus(id, status);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/update-lorry-invoice")
    public ResponseEntity<?> updateLorryReceipt(@RequestParam UUID id, @Valid @RequestBody LorryReceiptRequestDto dto) {
        try {
            LorryReceiptResponseDto updated = lorryReceiptService.updateLorryReceipt(id, dto);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete-lorry-invoice-by-id")
    public ResponseEntity<?> deleteLorryReceipt(@RequestParam UUID id) {
        try {
            lorryReceiptService.deleteLorryReceipt(id);
            return ResponseEntity.ok("LorryReceipt deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/details")
    public ResponseEntity<List<com.example.sudharshanlogistics.entity.dtos.lorryrInvoice.LorryInvoiceDetailsResponse>> getLorryInvoiceDetails(
            @RequestParam UUID consignerId,
            @RequestParam UUID consigneeId) {
        try {
            List<com.example.sudharshanlogistics.entity.dtos.lorryrInvoice.LorryInvoiceDetailsResponse> response = lorryReceiptService
                    .getLorryInvoiceDetails(consignerId, consigneeId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/send-invoice-email")
    public ResponseEntity<?> sendInvoiceEmail(@RequestParam UUID id) {
        try {
            lorryReceiptService.sendInvoiceWithPdf(id);
            return ResponseEntity.ok("Invoice email sent successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error sending email: " + e.getMessage());
        }
    }
}
