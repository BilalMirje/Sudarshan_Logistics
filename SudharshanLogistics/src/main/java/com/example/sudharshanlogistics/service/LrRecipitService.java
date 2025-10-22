package com.example.sudharshanlogistics.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.sudharshanlogistics.entity.dtos.lrInvoice.LrInvoiceRequestDto;
import com.example.sudharshanlogistics.entity.dtos.lrInvoice.LrInvoiceResponseDto;

public interface LrRecipitService {

  LrInvoiceResponseDto createLrInvoice(LrInvoiceRequestDto dto);

  LrInvoiceResponseDto getLrInvoice(UUID id);

  Page<LrInvoiceResponseDto> getAllLrInvoices(Pageable pageable);

  LrInvoiceResponseDto updateLrInvoice(UUID id, LrInvoiceRequestDto dto);

  boolean deleteLrInvoice(UUID id);

  List<LrInvoiceResponseDto> getLrReceiptsByBranchAndDate(String branchCode, LocalDate startDate, LocalDate endDate);

  String generateInvoiceDetails(UUID lrId);

  String generateReceiptDetails(UUID lrId);

  List<com.example.sudharshanlogistics.entity.dtos.RouteItemVehicleResponse> getRouteItemVehicleDetails(
      UUID consignerId, UUID consigneeId);

}
