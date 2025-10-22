package com.example.sudharshanlogistics.repository;

import com.example.sudharshanlogistics.entity.Privilege;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, UUID> {
}
