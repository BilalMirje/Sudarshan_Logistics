package com.example.sudharshanlogistics.entity.dtos.tax;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaxConfigurationRequestDto {
    @NotNull(message = "CGST percent is required")
    private BigDecimal cgstPercent;

    @NotNull(message = "SGST percent is required")
    private BigDecimal sgstPercent;

    @NotNull(message = "IGST percent is required")
    private BigDecimal igstPercent;

    @NotNull(message = "Effective from date is required")
    private LocalDate effectiveFrom;
}
