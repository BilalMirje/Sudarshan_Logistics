package com.example.sudharshanlogistics.service.Impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.example.sudharshanlogistics.entity.TaxConfiguration;
import com.example.sudharshanlogistics.entity.dtos.tax.TaxConfigurationRequestDto;
import com.example.sudharshanlogistics.entity.dtos.tax.TaxConfigurationResponseDto;
import com.example.sudharshanlogistics.repository.TaxConfigurationRepository;
import com.example.sudharshanlogistics.service.TaxConfigurationService;
import com.example.sudharshanlogistics.exception.NotFoundException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaxConfigurationServiceImpl implements TaxConfigurationService {

    private final TaxConfigurationRepository taxConfigurationRepository;
    private final ModelMapper modelMapper;

    @Override
    public TaxConfigurationResponseDto getLatestTaxConfig() {
        TaxConfiguration latest = taxConfigurationRepository.findTopByOrderByEffectiveFromDesc()
                .orElseThrow(() -> new RuntimeException("No tax configuration found"));
        return modelMapper.map(latest, TaxConfigurationResponseDto.class);
    }

    @Override
    public TaxConfigurationResponseDto createOrUpdateTaxConfig(TaxConfigurationRequestDto dto) {
        TaxConfiguration taxConfig = modelMapper.map(dto, TaxConfiguration.class);
        TaxConfiguration saved = taxConfigurationRepository.save(taxConfig);
        return modelMapper.map(saved, TaxConfigurationResponseDto.class);
    }

    @Override
    public TaxConfigurationResponseDto getTaxConfigById(UUID id) {
        TaxConfiguration entity = taxConfigurationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tax configuration not found with ID: " + id));
        return modelMapper.map(entity, TaxConfigurationResponseDto.class);
    }

    @Override
    public TaxConfigurationResponseDto updateTaxConfig(UUID id, TaxConfigurationRequestDto dto) {
        TaxConfiguration existing = taxConfigurationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tax configuration not found with ID: " + id));
        modelMapper.map(dto, existing);
        TaxConfiguration updated = taxConfigurationRepository.save(existing);
        return modelMapper.map(updated, TaxConfigurationResponseDto.class);
    }

    @Override
    public void deleteTaxConfig(UUID id) {
        if (!taxConfigurationRepository.existsById(id)) {
            throw new NotFoundException("Tax configuration not found with ID: " + id);
        }
        taxConfigurationRepository.deleteById(id);
    }
}
