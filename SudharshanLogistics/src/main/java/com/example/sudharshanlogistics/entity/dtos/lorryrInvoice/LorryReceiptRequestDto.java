package com.example.sudharshanlogistics.entity.dtos.lorryrInvoice;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LorryReceiptRequestDto {

    @NotNull(message = "Date is required")
    private LocalDate date;

    private List<UUID> itemIds;

    @NotBlank(message = "Collection type is required")
    private String collType;

    private String delvType;
    private String gcType;

    @NotBlank(message = "Mode of payment is required")
    private String modeOfPayment;

    @NotBlank(message = "Freight pay by is required")
    private String freightPayBy;

    @NotNull(message = "Actual weight is required")
    private Double actualWeight;

    @NotNull(message = "Rate per kg is required")
    private BigDecimal ratePerKg;

    @NotNull(message = "Total packages is required")
    private Integer totalPackages;

    @NotNull(message = "Invoice value is required")
    private BigDecimal invoiceValue;
}
