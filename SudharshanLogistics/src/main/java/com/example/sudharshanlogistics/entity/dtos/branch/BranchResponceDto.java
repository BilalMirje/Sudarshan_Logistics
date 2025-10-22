package com.example.sudharshanlogistics.entity.dtos.branch;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BranchResponceDto {

  private UUID branchId;
  private String branchCode;
  private String branchName;
}
