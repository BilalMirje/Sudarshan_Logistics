package com.example.sudharshanlogistics.entity.dtos.vehicle;

import java.util.UUID;

import com.example.sudharshanlogistics.entity.Vehicle.VehicleStatus;
import com.example.sudharshanlogistics.entity.Vehicle.VehicleType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VehicleRequestDto {

  @NotBlank(message = "Vehicle number is required")
  private String vehicleNumber;
  @NotBlank(message = "Model is required")
  private String model;
  @NotNull(message = "Capacity is required")
  private Double capacity;
  private VehicleStatus status; // AVAILABLE or ASSIGNED
  private String vehicleCode;
  private String driverName;
  private String driverLicenceNo;
  private String driverPermitNo;
  private String vehicleExpNo;
  private VehicleType type;
  private UUID ownerId;
  private UUID branchId;

}
