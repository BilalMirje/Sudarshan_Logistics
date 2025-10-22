package com.example.sudharshanlogistics.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.sudharshanlogistics.entity.Party;
import com.example.sudharshanlogistics.entity.dtos.party.PartyRequestDto;
import com.example.sudharshanlogistics.entity.dtos.party.PartyResponseDto;
import com.example.sudharshanlogistics.repository.PartyRepository;
import com.example.sudharshanlogistics.service.PartyService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/parties")
@RequiredArgsConstructor
@CrossOrigin("*")
public class PartyController {

  private final PartyService partyService;
  private final PartyRepository partyRepository;

  @PostMapping("/create-party")
  public ResponseEntity<?> createParty(@Valid @RequestBody PartyRequestDto partyRequestDto) {
    if (partyRepository.existsByGstNumber(partyRequestDto.getGstNumber())) {
      return ResponseEntity.status(HttpStatus.CONFLICT)
          .body(Map.of("error", "GST number already exists"));
    }
    return ResponseEntity.status(HttpStatus.CREATED).body(partyService.createParty(partyRequestDto));
  }

  @GetMapping("/get-party-by-id")
  public ResponseEntity<?> getPartyById(@RequestParam UUID partyId) {
    return ResponseEntity.ok(partyService.getPartyById(partyId));
  }

  @GetMapping("/get-all-parties")
  public ResponseEntity<?> getAllParties(@RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "partyCode") String sortBy,
      @RequestParam(defaultValue = "asc") String sortDir) {
    Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
        : Sort.by(sortBy).descending();
    Pageable pageable = PageRequest.of(page, size, sort);
    return ResponseEntity.ok(partyService.getAllParties(pageable));
  }

  @GetMapping("/search-party")
  public ResponseEntity<?> searchParties(@RequestParam String partyName) {
    List<PartyResponseDto> parties = partyService.searchParties(partyName);
    return ResponseEntity.ok(parties);
  }

  @PutMapping("/update-party")
  public ResponseEntity<?> updateParty(@RequestParam UUID partyId,
      @Valid @RequestBody PartyRequestDto partyRequestDto) {
    partyRepository.findById(partyId)
        .orElseThrow(() -> new RuntimeException("Party not found with id: " + partyId));
    Optional<Party> duplicateGstParty = partyRepository.findByGstNumber(partyRequestDto.getGstNumber());
    if (duplicateGstParty.isPresent() && !duplicateGstParty.get().getId().equals(partyId)) {
      return ResponseEntity.status(HttpStatus.CONFLICT)
          .body(Map.of("error", "GST number already exists for another party"));
    }
    return ResponseEntity.ok(partyService.updateParty(partyId, partyRequestDto));
  }

  @DeleteMapping("/delete-party")
  public ResponseEntity<?> deleteParty(@RequestParam UUID partyId) {
    partyService.deleteParty(partyId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/get-party-name")
  public ResponseEntity<?> getPartyNameById(@RequestParam UUID partyId) {
    return ResponseEntity.ok(partyService.getPartyNameById(partyId));
  }
}
