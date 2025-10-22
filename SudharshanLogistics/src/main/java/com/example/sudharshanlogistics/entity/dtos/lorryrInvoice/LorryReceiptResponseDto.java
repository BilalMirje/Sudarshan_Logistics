package com.example.sudharshanlogistics.entity.dtos.lorryrInvoice;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.example.sudharshanlogistics.entity.dtos.item.ItemResponseDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LorryReceiptResponseDto {

    private UUID id;
    private String wayBillNo;
    private LocalDate date;
    private List<ItemResponseDto> items;
    private String collType;
    private String delvType;
    private String gcType;
    private String modeOfPayment;
    private String freightPayBy;
    private Double actualWeight;
    private BigDecimal ratePerKg;
    private Integer totalPackages;
    private BigDecimal invoiceValue;
    private BigDecimal subTotal;
    private BigDecimal igstTax;
    private BigDecimal cgstTax;
    private BigDecimal sgstTax;
    private BigDecimal grandTotal;
    private String status;
}
