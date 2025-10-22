package com.example.sudharshanlogistics.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.sudharshanlogistics.entity.dtos.vehicle.VehicleRequestDto;
import com.example.sudharshanlogistics.entity.dtos.vehicle.VehicleResponseDto;
import com.example.sudharshanlogistics.service.VehicleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
@CrossOrigin("*")
public class VehicleController {

  private final VehicleService vehicleService;

  @PostMapping("/create-vehicle")
  public ResponseEntity<?> createVehicle(@RequestBody VehicleRequestDto requestDto) {
    return ResponseEntity.ok(vehicleService.createVehicle(requestDto));
  }

  @GetMapping("/get-vehicle-by-id")
  public ResponseEntity<?> getVehicleById(@RequestParam UUID id) {
    return ResponseEntity.ok(vehicleService.getVehicleById(id));
  }

  @GetMapping("/get-all-vehicles")
  public ResponseEntity<?> getAllVehicles(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "vehicleCode") String sortBy,
      @RequestParam(defaultValue = "asc") String sortDir) {

    Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
        : Sort.by(sortBy).descending();
    Pageable pageable = PageRequest.of(page, size, sort);
    Page<VehicleResponseDto> vehicles = vehicleService.getAllVehicles(pageable);
    return ResponseEntity.ok(vehicles);
  }

  @GetMapping("/search-vehicle")
  public ResponseEntity<?> searchVehicles(@RequestParam String vehicleNumber) {
    List<VehicleResponseDto> vehicles = vehicleService.searchVehicles(vehicleNumber);
    return ResponseEntity.ok(vehicles);
  }

  @PutMapping("/update-vehicle-by-id")
  public ResponseEntity<?> updateVehicle(@RequestParam UUID id,
      @RequestBody VehicleRequestDto requestDto) {
    return ResponseEntity.ok(vehicleService.updateVehicle(id, requestDto));
  }

  @DeleteMapping("/delete-vehicle-by-id")
  public ResponseEntity<?> deleteVehicle(@RequestParam UUID id) {
    vehicleService.deleteVehicle(id);
    return ResponseEntity.status(HttpStatus.OK).body("Vehicle Deleted Successfully");
  }
}
