package com.example.sudharshanlogistics.service.Impl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.sudharshanlogistics.entity.Owner;
import com.example.sudharshanlogistics.entity.dtos.owner.OwnerRequestDtos;
import com.example.sudharshanlogistics.entity.dtos.owner.OwnerResponceDtos;
import com.example.sudharshanlogistics.repository.OwnerRepository;
import com.example.sudharshanlogistics.service.OwnerService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class OwnerServiceImpl implements OwnerService {

  private final OwnerRepository ownerRepository;

  private OwnerResponceDtos mapToDto(Owner owner) {
    OwnerResponceDtos dto = new OwnerResponceDtos();
    dto.setOwnerId(owner.getId());
    dto.setOwnerName(owner.getOwnerName());
    dto.setContactNumber(owner.getContactNumber());
    dto.setAddress(owner.getAddress());
    dto.setEmail(owner.getEmail());
    return dto;
  }

  private Owner mapToEntity(OwnerRequestDtos dto) {
    Owner owner = new Owner();
    owner.setOwnerName(dto.getOwnerName());
    owner.setContactNumber(dto.getContactNumber());
    owner.setAddress(dto.getAddress());
    owner.setEmail(dto.getEmail());
    return owner;
  }

  @Override
  public OwnerResponceDtos createOwner(OwnerRequestDtos ownerRequestDtos) {
    ownerRepository
        .findByOwnerNameAndContactNumber(ownerRequestDtos.getOwnerName(), ownerRequestDtos.getContactNumber())
        .ifPresent(existingOwner -> {
          throw new IllegalArgumentException("Owner with the same name and contact number already exists");
        });
    Owner owner = mapToEntity(ownerRequestDtos);
    return mapToDto(ownerRepository.save(owner));
  }

  @Override
  public OwnerResponceDtos getOwnerById(UUID ownerId) {
    Owner owner = ownerRepository.findById(ownerId)
        .orElseThrow(() -> new EntityNotFoundException("Owner not found with id: " + ownerId));
    return mapToDto(owner);
  }

  @Override
  public Page<OwnerResponceDtos> getAllOwners(Pageable pageable) {
    Page<Owner> owners = ownerRepository.findAll(pageable);
    return owners
        .map(this::mapToDto);
  }

  @Override
  public List<OwnerResponceDtos> getOwnersByName(String ownerName) {
    List<Owner> owners = ownerRepository.findByOwnerNameContainingIgnoreCase(ownerName);
    if (owners.isEmpty()) {
      throw new RuntimeException("No owners found with name: " + ownerName);
    }
    return owners.stream()
        .map(this::mapToDto)
        .collect(Collectors.toList());
  }

  @Override
  public OwnerResponceDtos updateOwner(UUID ownerId, OwnerRequestDtos ownerRequestDtos) {
    Owner owner = ownerRepository.findById(ownerId)
        .orElseThrow(() -> new EntityNotFoundException("Owner not found with id: " + ownerId));
    ownerRepository
        .findByOwnerNameAndContactNumber(ownerRequestDtos.getOwnerName(), ownerRequestDtos.getContactNumber())
        .ifPresent(existingOwner -> {
          if (!existingOwner.getId().equals(ownerId)) {
            throw new IllegalArgumentException("Owner with the same name and contact number already exists");
          }
        });
    owner.setOwnerName(ownerRequestDtos.getOwnerName());
    owner.setContactNumber(ownerRequestDtos.getContactNumber());
    owner.setAddress(ownerRequestDtos.getAddress());
    owner.setEmail(ownerRequestDtos.getEmail());

    Owner updated = ownerRepository.save(owner);
    return mapToDto(updated);
  }

  @Override
  public void deleteOwner(UUID ownerId) {
    Owner owner = ownerRepository.findById(ownerId)
        .orElseThrow(() -> new EntityNotFoundException("Owner not found with id: " + ownerId));
    ownerRepository.delete(owner);
  }

  @Override
  public List<OwnerResponceDtos> serachOwners(String ownerName, String email) {
    List<Owner> owners = ownerRepository.findByOwnerNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
        ownerName,
        email);
    return owners.stream().map(this::mapToDto).toList();
  }
}
