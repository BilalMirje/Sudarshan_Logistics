package com.example.sudharshanlogistics.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.sudharshanlogistics.entity.dtos.lorryrInvoice.LorryReceiptRequestDto;
import com.example.sudharshanlogistics.entity.dtos.lorryrInvoice.LorryReceiptResponseDto;

public interface LorryInvoiceService {
    LorryReceiptResponseDto createLorryReceipt(LorryReceiptRequestDto dto);

    LorryReceiptResponseDto getLorryReceiptById(UUID id);

    LorryReceiptResponseDto getLorryReceiptByWayBillNo(String wayBillNo);

    Page<LorryReceiptResponseDto> getAllLorryReceipts(Pageable pageable);

    LorryReceiptResponseDto updateLorryReceiptStatus(UUID id, String status);

    LorryReceiptResponseDto updateLorryReceipt(UUID id, LorryReceiptRequestDto dto);

    void deleteLorryReceipt(UUID id);

    void sendInvoiceWithPdf(UUID id);
    List<com.example.sudharshanlogistics.entity.dtos.lorryrInvoice.LorryInvoiceDetailsResponse> getLorryInvoiceDetails(
            UUID consignerId, UUID consigneeId);
}
