package com.example.sudharshanlogistics.service.Impl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.sudharshanlogistics.entity.Branch;
import com.example.sudharshanlogistics.entity.Owner;
import com.example.sudharshanlogistics.entity.Vehicle;
import com.example.sudharshanlogistics.entity.dtos.branch.BranchResponceDto;
import com.example.sudharshanlogistics.entity.dtos.owner.OwnerResponceDtos;
import com.example.sudharshanlogistics.entity.dtos.vehicle.VehicleRequestDto;
import com.example.sudharshanlogistics.entity.dtos.vehicle.VehicleResponseDto;
import com.example.sudharshanlogistics.repository.BranchRepository;
import com.example.sudharshanlogistics.repository.OwnerRepository;
import com.example.sudharshanlogistics.repository.VehicleRepository;
import com.example.sudharshanlogistics.service.VehicleService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class VehicleServiceImpl implements VehicleService {

  private final VehicleRepository vehicleRepository;
  private final OwnerRepository ownerRepository;
  private final BranchRepository branchRepository;

  private VehicleResponseDto mapToDto(Vehicle vehicle) {
    VehicleResponseDto dto = new VehicleResponseDto();
    dto.setVehicleId(vehicle.getId());
    dto.setVehicleNumber(vehicle.getVehicleNumber());
    dto.setVehicleCode(vehicle.getVehicleCode());
    dto.setDriverName(vehicle.getDriverName());
    dto.setDriverLicenceNo(vehicle.getDriverLicenceNo());
    dto.setDriverPermitNo(vehicle.getDriverPermitNo());
    dto.setModel(vehicle.getModel());
    dto.setCapacity(vehicle.getCapacity());
    dto.setVehicleExpNo(vehicle.getVehicleExpNo());
    dto.setType(vehicle.getType());
    dto.setStatus(vehicle.getStatus());
    dto.setOwner(mapOwnerToDto(vehicle.getOwner()));
    dto.setBranch(mapBranchToDTo(vehicle.getBranch()));
    return dto;
  }

  private OwnerResponceDtos mapOwnerToDto(Owner owner) {
    if (owner == null)
      return null;
    OwnerResponceDtos dto = new OwnerResponceDtos();
    dto.setOwnerId(owner.getId());
    dto.setOwnerName(owner.getOwnerName());
    dto.setContactNumber(owner.getContactNumber());
    dto.setAddress(owner.getAddress());
    dto.setEmail(owner.getEmail());
    return dto;
  }

  private BranchResponceDto mapBranchToDTo(Branch branch) {
    if (branch == null)
      return null;
    BranchResponceDto dto = new BranchResponceDto();
    dto.setBranchId(branch.getId());
    dto.setBranchCode(branch.getBranchCode());
    dto.setBranchName(branch.getBranchName());
    return dto;
  }

  private Vehicle mapToEntity(VehicleRequestDto dto) {
    Vehicle vehicle = new Vehicle();
    vehicle.setVehicleNumber(dto.getVehicleNumber());
    vehicle.setVehicleCode(dto.getVehicleCode());
    vehicle.setDriverName(dto.getDriverName());
    vehicle.setDriverLicenceNo(dto.getDriverLicenceNo());
    vehicle.setDriverPermitNo(dto.getDriverPermitNo());
    vehicle.setModel(dto.getModel());
    vehicle.setCapacity(dto.getCapacity());
    vehicle.setVehicleExpNo(dto.getVehicleExpNo());
    vehicle.setType(dto.getType());
    vehicle.setStatus(dto.getStatus());
    return vehicle;
  }

  @Override
  public VehicleResponseDto createVehicle(VehicleRequestDto requestDto) {
    Vehicle vehicle = mapToEntity(requestDto);
    Owner owner = ownerRepository.findById(requestDto.getOwnerId())
        .orElseThrow(() -> new RuntimeException("Owner not found with id: " + requestDto.getOwnerId()));
    Branch branch = branchRepository.findById(requestDto.getBranchId())
        .orElseThrow(() -> new RuntimeException("Branch not found with id: " + requestDto.getBranchId()));
    vehicle.setOwner(owner);
    vehicle.setBranch(branch);
    return mapToDto(vehicleRepository.save(vehicle));
  }

  @Override
  public VehicleResponseDto getVehicleById(UUID id) {
    Vehicle vehicle = vehicleRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Vehicle not found with id: " + id));
    return mapToDto(vehicle);
  }

  @Override
  public Page<VehicleResponseDto> getAllVehicles(Pageable pageable) {
    Page<Vehicle> vehicles = vehicleRepository.findAll(pageable);
    return vehicles
        .map(this::mapToDto);
  }

  @Override
  public List<VehicleResponseDto> searchVehicles(String vehicleNumber) {
    List<Vehicle> vehicles = vehicleRepository.findByVehicleNumberContainingIgnoreCase(vehicleNumber);
    return vehicles.stream()
        .map(this::mapToDto)
        .collect(Collectors.toList());
  }

  @Override
  public VehicleResponseDto updateVehicle(UUID id, VehicleRequestDto requestDto) {
    Vehicle vehicle = vehicleRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Vehicle not found with id: " + id));
    Owner newOwner = ownerRepository.findById(requestDto.getOwnerId())
        .orElseThrow(() -> new RuntimeException("Owner is not found with id: " + requestDto.getOwnerId()));
    Branch branch = branchRepository.findById(requestDto.getBranchId())
        .orElseThrow(() -> new RuntimeException("Branch not found with id: " + requestDto.getBranchId()));
    vehicle.setVehicleNumber(requestDto.getVehicleNumber());
    vehicle.setVehicleCode(requestDto.getVehicleCode());
    vehicle.setDriverName(requestDto.getDriverName());
    vehicle.setDriverLicenceNo(requestDto.getDriverLicenceNo());
    vehicle.setDriverPermitNo(requestDto.getDriverPermitNo());
    vehicle.setModel(requestDto.getModel());
    vehicle.setCapacity(requestDto.getCapacity());
    vehicle.setVehicleExpNo(requestDto.getVehicleExpNo());
    vehicle.setType(requestDto.getType());
    vehicle.setStatus(requestDto.getStatus());
    vehicle.setOwner(newOwner);
    vehicle.setBranch(branch);
    Vehicle updated = vehicleRepository.save(vehicle);
    return mapToDto(updated);
  }

  @Override
  public void deleteVehicle(UUID id) {
    Vehicle vehicle = vehicleRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Vehicle not found with id: " + id));
    vehicleRepository.delete(vehicle);
  }
}
