package com.example.sudharshanlogistics.entity;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "vehicle_table")
@Builder
public class Vehicle extends Audit {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;
  private String vehicleNumber;
  private String vehicleCode;
  private String driverName;

  // @Pattern(regexp = "^[A-Z0-9]{4}(-[A-Z0-9]{4}){3}$", message = "Invalid
  // Licence No format.Expected format:AB12-CD34-EF56-GH78")
  private String driverLicenceNo;

  // @Pattern(regexp =
  // "[A-Z]{2}\\d{2}-[A-Z]{2}\\d{2}-[A-Z]{2}\\d{2}-[A-Z]{2}\\d{2}", message =
  // "Invalid Licence No format. Expected format: AB12-CD34-EF56-GH78")
  private String driverPermitNo;
  private String model;
  private double capacity; // in tons or kg

  // @Pattern(regexp = "VEH-\\d{4}-[A-Z0-9]{4}", message = "Invalid vehicleExp
  // Noformat.Expected format:VEH-2025-A1B2")
  private String vehicleExpNo;

  @ManyToOne
  private Owner owner;

  @ManyToOne
  @JoinColumn(name = "branch_id")
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private Branch branch;

  @Enumerated(EnumType.STRING)
  private VehicleType type;

  public enum VehicleType {
    HIRED,
    OWNER
  }

  @Enumerated(EnumType.STRING)
  private VehicleStatus status;

  public enum VehicleStatus {
    AVAILABLE, ASSIGNED
  }
}
