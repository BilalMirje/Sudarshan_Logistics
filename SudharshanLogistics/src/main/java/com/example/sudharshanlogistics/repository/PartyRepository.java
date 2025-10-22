package com.example.sudharshanlogistics.repository;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.sudharshanlogistics.entity.Party;

@Repository
public interface PartyRepository extends JpaRepository<Party, UUID> {

  Optional<Party> findByPartyCode(String partyCode);

  boolean existsByPartyNumber(String partyNumber);

  Optional<Party> findByGstNumber(String gstNumber);

  boolean existsByGstNumber(String gstNumber);

  List<Party> findByPartyNameContainingIgnoreCase(String name);

  @Query("SELECT p.partyName FROM Party p WHERE p.id = :id")
  String findPartyNameById(UUID id);
}
