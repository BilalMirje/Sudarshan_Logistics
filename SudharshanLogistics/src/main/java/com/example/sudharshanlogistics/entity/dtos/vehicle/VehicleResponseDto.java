package com.example.sudharshanlogistics.entity.dtos.vehicle;

import java.util.UUID;

import com.example.sudharshanlogistics.entity.dtos.branch.BranchResponceDto;
import com.example.sudharshanlogistics.entity.dtos.owner.OwnerResponceDtos;
import com.example.sudharshanlogistics.entity.Vehicle.VehicleStatus;
import com.example.sudharshanlogistics.entity.Vehicle.VehicleType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VehicleResponseDto {

  private UUID vehicleId;
  private String vehicleNumber;
  private String model;
  private Double capacity;
  private VehicleStatus status;
  private OwnerResponceDtos owner;
  private BranchResponceDto branch;
  private String vehicleCode;
  private String driverName;
  private String driverLicenceNo;
  private String driverPermitNo;
  private String vehicleExpNo;
  private VehicleType type;
}
