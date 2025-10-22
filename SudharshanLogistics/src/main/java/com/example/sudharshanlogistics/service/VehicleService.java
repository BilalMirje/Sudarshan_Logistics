package com.example.sudharshanlogistics.service;

import com.example.sudharshanlogistics.entity.dtos.vehicle.VehicleRequestDto;
import com.example.sudharshanlogistics.entity.dtos.vehicle.VehicleResponseDto;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VehicleService {
  VehicleResponseDto createVehicle(VehicleRequestDto requestDto);

  VehicleResponseDto getVehicleById(UUID id);

  Page<VehicleResponseDto> getAllVehicles(Pageable pageable);

  List<VehicleResponseDto> searchVehicles(String vehicleNumber);

  VehicleResponseDto updateVehicle(UUID id, VehicleRequestDto requestDto);

  void deleteVehicle(UUID id);
}
