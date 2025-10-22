package com.example.sudharshanlogistics.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.sudharshanlogistics.entity.dtos.lrInvoice.LrInvoiceRequestDto;
import com.example.sudharshanlogistics.entity.dtos.lrInvoice.LrInvoiceResponseDto;
import com.example.sudharshanlogistics.service.LrRecipitService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping("/api/lr-invoice")
@Tag(name = "Lr-Invoice-Controller", description = "You can create, read, update, delete LR Invoices")
public class LrInvoiceController {

  private final LrRecipitService lrInvoiceService;


  @PostMapping("/create-lrInvoice")
  @Operation(summary = "Create LR Invoice", description = "Create a new LR Invoice with validation for unique cNoteNo and invoiceNo")
  public ResponseEntity<?> createLrInvoice(@Valid @RequestBody LrInvoiceRequestDto dto) {

    LrInvoiceResponseDto response = lrInvoiceService.createLrInvoice(dto);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/get-lrInvoice-by-id")
  @Operation(summary = "Get LR Invoice", description = "Get LR Invoice by ID")
  public ResponseEntity<?> getLrInvoice(@RequestParam UUID id) {
    LrInvoiceResponseDto response = lrInvoiceService.getLrInvoice(id);
    if (response != null) {
      return ResponseEntity.ok(response);
    }
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("LR Invoice not found");
  }

  @GetMapping("/get-all-lrInvoice")
  @Operation(summary = "Get All LR Invoices", description = "Get all LR Invoices with pagination")
  public ResponseEntity<?> getAllLrInvoices(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "cNoteNo") String sortBy,
      @RequestParam(defaultValue = "asc") String sortDir) {
    Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
    Pageable pageable = PageRequest.of(page, size, sort);
    Page<LrInvoiceResponseDto> response = lrInvoiceService.getAllLrInvoices(pageable);
    return ResponseEntity.ok(response);
  }

  @PutMapping("/update-lrInvoice-by-id")
  @Transactional
  @Operation(summary = "Update LR Invoice", description = "Update LR Invoice by ID with validation for unique cNoteNo and invoiceNo")
  public ResponseEntity<?> updateLrInvoice(@RequestParam UUID id, @Valid @RequestBody LrInvoiceRequestDto dto) {
    try {
      LrInvoiceResponseDto response = lrInvoiceService.updateLrInvoice(id, dto);
      if (response != null) {
        return ResponseEntity.ok(response);
      }
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("LR Invoice not found");
    } catch (RuntimeException e) {
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  @DeleteMapping("/delete-lrInvoice-by-id")
  @Operation(summary = "Delete LR Invoice", description = "Delete LR Invoice by ID")
  public ResponseEntity<?> deleteLrInvoice(@RequestParam UUID id) {
    boolean deleted = lrInvoiceService.deleteLrInvoice(id);
    if (deleted) {
      return ResponseEntity.ok("LR Invoice deleted successfully");
    }
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("LR Invoice not found");
  }

  @GetMapping("/get-lrreceipts-by-branch-and-date")
  @Operation(summary = "Get LR Receipts by Branch and Date Range", description = "Get all LR Receipts for a given branch code and date range")
  public ResponseEntity<?> getLrReceiptsByBranchAndDate(
      @RequestParam String branchCode,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
    try {
      List<LrInvoiceResponseDto> response = lrInvoiceService.getLrReceiptsByBranchAndDate(branchCode, startDate,
          endDate);
      return ResponseEntity.ok(response);
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }


  @GetMapping("/routes")
  @Operation(summary = "Get Route Item Vehicle Details", description = "Get route, item, and vehicle details based on consigner and consignee IDs")
  public ResponseEntity<List<com.example.sudharshanlogistics.entity.dtos.RouteItemVehicleResponse>> getRouteItemVehicleDetails(
      @RequestParam UUID consignerId,
      @RequestParam UUID consigneeId) {
    try {
      List<com.example.sudharshanlogistics.entity.dtos.RouteItemVehicleResponse> response = lrInvoiceService
          .getRouteItemVehicleDetails(consignerId, consigneeId);
      return ResponseEntity.ok(response);
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
  }
}
