package com.example.sudharshanlogistics.service;

import com.example.sudharshanlogistics.entity.dtos.owner.OwnerRequestDtos;
import com.example.sudharshanlogistics.entity.dtos.owner.OwnerResponceDtos;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OwnerService {
  OwnerResponceDtos createOwner(OwnerRequestDtos ownerRequestDtos);

  OwnerResponceDtos getOwnerById(UUID ownerId);

  Page<OwnerResponceDtos> getAllOwners(Pageable pageable);

  List<OwnerResponceDtos> getOwnersByName(String ownerName);

  OwnerResponceDtos updateOwner(UUID ownerId, OwnerRequestDtos ownerRequestDtos);

  void deleteOwner(UUID ownerId);

  List<OwnerResponceDtos> serachOwners(String ownerName, String email);
}
