package com.example.sudharshanlogistics.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.sudharshanlogistics.entity.dtos.branch.BranchRequestDto;
import com.example.sudharshanlogistics.entity.dtos.branch.BranchResponceDto;

public interface BranchService {

  BranchResponceDto createBranch(BranchRequestDto branchRequestDto);

  BranchResponceDto getBranchById(UUID id);

  Page<BranchResponceDto> getAllBranches(Pageable pageable);

  List<BranchResponceDto> searchBranches(String name, String branchCode);

  BranchResponceDto updateBranch(UUID id, BranchRequestDto branchRequestDto);

  void deleteBranch(UUID id);
}
