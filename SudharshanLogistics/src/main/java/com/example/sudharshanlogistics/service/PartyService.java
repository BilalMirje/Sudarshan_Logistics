package com.example.sudharshanlogistics.service;

import com.example.sudharshanlogistics.entity.dtos.party.PartyRequestDto;
import com.example.sudharshanlogistics.entity.dtos.party.PartyResponseDto;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PartyService {
  PartyResponseDto createParty(PartyRequestDto partyRequestDto);

  PartyResponseDto getPartyById(UUID partyId);

  Page<PartyResponseDto> getAllParties(Pageable pageable);

  List<PartyResponseDto> searchParties(String name);

  PartyResponseDto updateParty(UUID partyId, PartyRequestDto partyRequestDto);

  void deleteParty(UUID partyId);

  String getPartyNameById(UUID partyId);
}
