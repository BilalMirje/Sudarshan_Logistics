package com.example.sudharshanlogistics.entity.dtos.party;

import java.util.UUID;

import com.example.sudharshanlogistics.entity.dtos.branch.BranchResponceDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PartyResponseDto {

  private UUID id;
  private String partyCode;
  private String partyName;
  private String partyNumber;
  private String partyAddress;
  private String gstNumber;
  private String district;
  private String pinCode;
  private String stateCode;
  private String vendorMailId;
  private String division;
  private BranchResponceDto branchDto;

}
