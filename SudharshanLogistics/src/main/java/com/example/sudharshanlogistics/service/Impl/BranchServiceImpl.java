package com.example.sudharshanlogistics.service.Impl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.sudharshanlogistics.entity.Branch;
import com.example.sudharshanlogistics.entity.dtos.branch.BranchRequestDto;
import com.example.sudharshanlogistics.entity.dtos.branch.BranchResponceDto;
import com.example.sudharshanlogistics.repository.BranchRepository;
import com.example.sudharshanlogistics.service.BranchService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BranchServiceImpl implements BranchService {

  private final BranchRepository branchRepository;

  private BranchResponceDto mapToDto(Branch branch) {
    return BranchResponceDto.builder()
        .branchId(branch.getId())
        .branchCode(branch.getBranchCode())
        .branchName(branch.getBranchName())
        .build();
  }

  private Branch mapToEntity(BranchRequestDto dto) {
    return Branch.builder()
        .branchCode(dto.getBranchCode())
        .branchName(dto.getBranchName())
        .build();
  }

  @Override
  public BranchResponceDto createBranch(BranchRequestDto branchRequestDto) {
    try {
      Branch branch = mapToEntity(branchRequestDto);
      return mapToDto(branchRepository.save(branch));
    } catch (DataIntegrityViolationException e) {
      throw new RuntimeException("This branch code is already in use!!!");
    }
  }

  @Override
  public BranchResponceDto getBranchById(UUID id) {
    Branch branch = branchRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Branch not found with id: " + id));
    return mapToDto(branch);
  }

  @Override
  public Page<BranchResponceDto> getAllBranches(Pageable pageable) {
    Page<Branch> branches = branchRepository.findAll(pageable);
    return branches.map(this::mapToDto);
  }

  @Override
  public List<BranchResponceDto> searchBranches(String branchName, String branchCode) {
    List<Branch> branches = branchRepository.findByBranchNameContainingIgnoreCaseOrBranchCodeContainingIgnoreCase(
        branchName,
        branchCode);
    return branches.stream().map(this::mapToDto).collect(Collectors.toList());
  }

  @Override
  public BranchResponceDto updateBranch(UUID id, BranchRequestDto branchRequestDto) {
    try {
      Branch existingBranch = branchRepository.findById(id)
          .orElseThrow(() -> new EntityNotFoundException("Branch not found with id: " + id));
      existingBranch.setBranchCode(branchRequestDto.getBranchCode());
      existingBranch.setBranchName(branchRequestDto.getBranchName());
      Branch updatedBranch = branchRepository.save(existingBranch);
      return mapToDto(updatedBranch);
    } catch (DataIntegrityViolationException e) {
      throw new RuntimeException("This branch code is already in use!!!");
    }
  }

  @Override
  public void deleteBranch(UUID id) {
    Branch branch = branchRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Branch not found with id: " + id));
    branchRepository.delete(branch);
  }
}
