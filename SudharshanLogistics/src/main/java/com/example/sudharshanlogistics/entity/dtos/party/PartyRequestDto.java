package com.example.sudharshanlogistics.entity.dtos.party;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PartyRequestDto {

  private String partyCode;

  private String partyName;

  private String partyNumber;

  private String partyAddress;

  @Pattern(regexp = "^([0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1})$", message = "Invalid GST number format")
  private String gstNumber;

  private String district;
  private String division;

  @NotNull(message = "Pin code is required")
  private String pinCode;

  private String stateCode;

  private String vendorMailId;

  private UUID branchId;

}
