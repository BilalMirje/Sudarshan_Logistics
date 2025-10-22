package com.example.sudharshanlogistics.service.Impl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.sudharshanlogistics.entity.Branch;
import com.example.sudharshanlogistics.entity.Party;
import com.example.sudharshanlogistics.entity.dtos.branch.BranchResponceDto;
import com.example.sudharshanlogistics.entity.dtos.party.PartyRequestDto;
import com.example.sudharshanlogistics.entity.dtos.party.PartyResponseDto;
import com.example.sudharshanlogistics.repository.AccountRepository;
import com.example.sudharshanlogistics.repository.BranchRepository;
import com.example.sudharshanlogistics.repository.PartyRepository;
import com.example.sudharshanlogistics.service.PartyService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PartyServiceImpl implements PartyService {

  private final PartyRepository partyRepository;
  private final BranchRepository branchRepository;
  private final AccountRepository accountRepository;

  private PartyResponseDto mapToDto(Party party) {
    PartyResponseDto dto = new PartyResponseDto();
    dto.setId(party.getId());
    dto.setPartyCode(party.getPartyCode());
    dto.setPartyName(party.getPartyName());
    dto.setPartyNumber(party.getPartyNumber());
    dto.setPartyAddress(party.getPartyAddress());
    dto.setGstNumber(party.getGstNumber());
    dto.setDistrict(party.getDistrict());
    dto.setPinCode(party.getPinCode());
    dto.setDivision(party.getDivision());
    dto.setStateCode(party.getStateCode());
    dto.setVendorMailId(party.getVendorMailId());
    dto.setBranchDto(mapToBranchDto(party.getBranch()));
    return dto;
  }

  private BranchResponceDto mapToBranchDto(Branch branch) {
    if (branch == null)
      return null;
    BranchResponceDto dto = new BranchResponceDto();
    dto.setBranchId(branch.getId());
    dto.setBranchCode(branch.getBranchCode());
    dto.setBranchName(branch.getBranchName());
    return dto;
  }

  private Party mapToEntity(PartyRequestDto dto) {
    Party party = new Party();
    party.setPartyCode(dto.getPartyCode());
    party.setPartyName(dto.getPartyName());
    party.setPartyNumber(dto.getPartyNumber());
    party.setPartyAddress(dto.getPartyAddress());
    party.setGstNumber(dto.getGstNumber());
    party.setDistrict(dto.getDistrict());
    party.setPinCode(dto.getPinCode());
    party.setDivision(dto.getDivision());
    party.setStateCode(dto.getStateCode());
    party.setVendorMailId(dto.getVendorMailId());
    Branch branch = branchRepository.findById(dto.getBranchId())
        .orElseThrow(() -> new RuntimeException("Branch not found with id: " + dto.getBranchId()));
    party.setBranch(branch);

    return party;
  }

  @Override
  public PartyResponseDto createParty(PartyRequestDto partyRequestDto) {
    // Check for existing party with same partyCode if needed
    partyRepository.findByPartyCode(partyRequestDto.getPartyCode())
        .ifPresent(existingParty -> {
          throw new IllegalArgumentException("Party with the same code already exists");
        });
    Party party = mapToEntity(partyRequestDto);
    return mapToDto(partyRepository.save(party));
  }

  @Override
  public PartyResponseDto getPartyById(UUID partyId) {
    Party party = partyRepository.findById(partyId)
        .orElseThrow(() -> new EntityNotFoundException("Party not found with id: " + partyId));
    return mapToDto(party);
  }

  @Override
  public Page<PartyResponseDto> getAllParties(Pageable pageable) {
    Page<Party> parties = partyRepository.findAll(pageable);
    return parties
        .map(this::mapToDto);
  }

  @Override
  public List<PartyResponseDto> searchParties(String partyName) {
    List<Party> parties = partyRepository.findByPartyNameContainingIgnoreCase(partyName);
    return parties.stream()
        .map(this::mapToDto)
        .collect(Collectors.toList());
  }

  @Override
  public PartyResponseDto updateParty(UUID partyId, PartyRequestDto partyRequestDto) {
    Party party = partyRepository.findById(partyId)
        .orElseThrow(() -> new EntityNotFoundException("Party not found with id: " + partyId));
    Branch branch = branchRepository.findById(partyRequestDto.getBranchId())
        .orElseThrow(() -> new RuntimeException("Branch not found with id: " + partyRequestDto.getBranchId()));
    partyRepository.findByPartyCode(partyRequestDto.getPartyCode())
        .ifPresent(existingParty -> {
          if (!existingParty.getId().equals(partyId)) {
            throw new IllegalArgumentException("Party with the same code already exists");
          }
        });
    party.setPartyCode(partyRequestDto.getPartyCode());
    party.setPartyName(partyRequestDto.getPartyName());
    party.setPartyNumber(partyRequestDto.getPartyNumber());
    party.setPartyAddress(partyRequestDto.getPartyAddress());
    party.setGstNumber(partyRequestDto.getGstNumber());
    party.setDistrict(partyRequestDto.getDistrict());
    party.setPinCode(partyRequestDto.getPinCode());
    party.setDivision(partyRequestDto.getDivision());
    party.setStateCode(partyRequestDto.getStateCode());
    party.setVendorMailId(partyRequestDto.getVendorMailId());
    party.setBranch(branch);
    Party updated = partyRepository.save(party);
    return mapToDto(updated);
  }

  @Override
  public void deleteParty(UUID partyId) {
    Party party = partyRepository.findById(partyId)
        .orElseThrow(() -> new EntityNotFoundException("Party not found with id: " + partyId));
    partyRepository.delete(party);
  }

  @Override
  public String getPartyNameById(UUID partyId) {
    return partyRepository.findPartyNameById(partyId);
  }
}
