package com.example.sudharshanlogistics.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.sudharshanlogistics.entity.Owner;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, UUID> {
  Optional<Owner> findByOwnerNameAndContactNumber(String ownerName, String contactNumber);

  List<Owner> findByOwnerNameContainingIgnoreCase(String ownerName);

  List<Owner> findByOwnerNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String ownerName,
      String email);

}
