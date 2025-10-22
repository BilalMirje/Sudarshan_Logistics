package com.example.sudharshanlogistics.entity.dtos.lrInvoice;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.example.sudharshanlogistics.entity.LrRecepit.PaymentMode;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LrInvoiceRequestDto {

  @NotNull(message = "Date is required")
  private LocalDate date;

  @NotNull(message = "Payment Mode is required")
  private PaymentMode paymentMode;

  private boolean insured;

  private List<UUID> itemId;
  private UUID partyId;
  private UUID consigneeId;

  private double doorDeliveryCharge;
  private double doorPickupCharge;
  private double hamaliCharge;
  private double statisticalCharge;
  private double serviceCharge;

  @NotBlank(message = "Invoice No is required")
  private String invoiceNo;

  private double invoiceValue;
  private String amountInWords;
}
