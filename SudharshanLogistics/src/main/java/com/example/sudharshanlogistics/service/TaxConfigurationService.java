package com.example.sudharshanlogistics.service;

import com.example.sudharshanlogistics.entity.dtos.tax.TaxConfigurationRequestDto;
import com.example.sudharshanlogistics.entity.dtos.tax.TaxConfigurationResponseDto;

import java.util.UUID;

public interface TaxConfigurationService {

    TaxConfigurationResponseDto getLatestTaxConfig();

    TaxConfigurationResponseDto createOrUpdateTaxConfig(TaxConfigurationRequestDto dto);

    TaxConfigurationResponseDto getTaxConfigById(UUID id);

    TaxConfigurationResponseDto updateTaxConfig(UUID id, TaxConfigurationRequestDto dto);

    void deleteTaxConfig(UUID id);
}
