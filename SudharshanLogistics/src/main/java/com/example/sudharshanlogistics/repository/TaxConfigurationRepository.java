package com.example.sudharshanlogistics.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.sudharshanlogistics.entity.TaxConfiguration;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaxConfigurationRepository extends JpaRepository<TaxConfiguration, UUID> {
    Optional<TaxConfiguration> findTopByOrderByEffectiveFromDesc();
}
