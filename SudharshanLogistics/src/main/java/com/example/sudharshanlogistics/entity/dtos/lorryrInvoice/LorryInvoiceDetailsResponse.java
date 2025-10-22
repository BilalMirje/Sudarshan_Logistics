package com.example.sudharshanlogistics.entity.dtos.lorryrInvoice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LorryInvoiceDetailsResponse {
  private String consignerGstNumber;
  private String consigneeGstNumber;
  private String startLocation;
  private String endLocation;
  private String itemName;
  private String itemDescription;
  private Integer itemQuantity;
  private String vehicleNo;
}
