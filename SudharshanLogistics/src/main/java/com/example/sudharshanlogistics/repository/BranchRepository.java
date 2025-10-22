package com.example.sudharshanlogistics.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.sudharshanlogistics.entity.Branch;

@Repository
public interface BranchRepository extends JpaRepository<Branch, UUID> {

  List<Branch> findByBranchNameContainingIgnoreCaseOrBranchCodeContainingIgnoreCase(String branchName,
      String branchCode);

  Optional<Branch> findByBranchCode(String branchCode);
}
