package com.example.sudharshanlogistics.entity.dtos.branch;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BranchRequestDto {

  private String branchCode;
  private String branchName;
}
