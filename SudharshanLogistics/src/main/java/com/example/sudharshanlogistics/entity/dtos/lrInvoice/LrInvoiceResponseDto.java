package com.example.sudharshanlogistics.entity.dtos.lrInvoice;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import com.example.sudharshanlogistics.entity.dtos.item.ItemResponseDto;
import com.example.sudharshanlogistics.entity.dtos.party.PartyResponseDto;
import com.example.sudharshanlogistics.entity.LrRecepit.PaymentMode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LrInvoiceResponseDto {

  private UUID id;
  private String cNoteNo;
  private LocalDate date;
  private PaymentMode paymentMode;
  private boolean insured;
  private List<ItemResponseDto> items;
  private PartyResponseDto party;
  private PartyResponseDto consignee;
  private double doorDeliveryCharge;
  private double doorPickupCharge;
  private double hamaliCharge;
  private double statisticalCharge;
  private double serviceCharge;
  private double totalFreight;
  private String invoiceNo;
  private double invoiceValue;
  private String amountInWords;
  private String transportId;
}
