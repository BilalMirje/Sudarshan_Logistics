package com.example.sudharshanlogistics.repository;

import com.example.sudharshanlogistics.entity.EmailConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmailConfigRepository extends JpaRepository<EmailConfig, UUID> {
    @Modifying
    @Query("UPDATE EmailConfig e SET e.active = false WHERE e.active = true")
    void deactivateAll();

    Optional<EmailConfig> findByActiveTrue();
}