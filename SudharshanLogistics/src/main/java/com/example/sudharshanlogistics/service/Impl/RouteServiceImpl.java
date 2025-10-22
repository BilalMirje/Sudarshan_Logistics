package com.example.sudharshanlogistics.service.Impl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.sudharshanlogistics.entity.Route;
import com.example.sudharshanlogistics.entity.Party;
import com.example.sudharshanlogistics.entity.Vehicle;
import com.example.sudharshanlogistics.entity.Branch;
import com.example.sudharshanlogistics.entity.Owner;
import com.example.sudharshanlogistics.entity.dtos.branch.BranchResponceDto;
import com.example.sudharshanlogistics.entity.dtos.owner.OwnerResponceDtos;
import com.example.sudharshanlogistics.entity.dtos.route.RouteRequestDtos;

import com.example.sudharshanlogistics.entity.dtos.route.RouteResponseDto;
import com.example.sudharshanlogistics.entity.dtos.party.PartyResponseDto;
import com.example.sudharshanlogistics.entity.dtos.vehicle.VehicleResponseDto;
import com.example.sudharshanlogistics.repository.RouteRepository;
import com.example.sudharshanlogistics.repository.PartyRepository;
import com.example.sudharshanlogistics.repository.VehicleRepository;
import com.example.sudharshanlogistics.service.RouteService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class RouteServiceImpl implements RouteService {

  private final RouteRepository routeRepository;
  private final PartyRepository partyRepository;
  private final VehicleRepository vehicleRepository;

  public RouteResponseDto mapToDto(Route route) {
    return RouteResponseDto.builder()
        .id(route.getId())
        .routeNumber(route.getRouteNumber())
        .routeName(route.getRouteName())
        .startLocation(route.getStartLocation())
        .endLocation(route.getEndLocation())
        .type(route.getType())
        .consigneeId(mapPartyToDto(route.getConsigneeId()))
        .consignerId(mapPartyToDto(route.getConsignerId()))
        .vehicle(mapVehicleToDto(route.getVehicle()))
        .build();
  }

  private BranchResponceDto mapToBranchDto(Branch branch) {
    if (branch == null)
      return null;
    BranchResponceDto dto = new BranchResponceDto();
    dto.setBranchId(branch.getId());
    dto.setBranchCode(branch.getBranchCode());
    dto.setBranchName(branch.getBranchName());
    return dto;
  }

  private PartyResponseDto mapPartyToDto(Party party) {
    if (party == null)
      return null;
    return PartyResponseDto.builder()
        .id(party.getId())
        .partyCode(party.getPartyCode())
        .partyName(party.getPartyName())
        .partyNumber(party.getPartyNumber())
        .partyAddress(party.getPartyAddress())
        .district(party.getDistrict())
        .division(party.getDivision())
        .pinCode(party.getPinCode())
        .stateCode(party.getStateCode())
        .vendorMailId(party.getVendorMailId())
        .gstNumber(party.getGstNumber())
        .branchDto(mapToBranchDto(party.getBranch()))
        .build();
  }

  private VehicleResponseDto mapVehicleToDto(Vehicle vehicle) {
    if (vehicle == null)
      return null;
    return VehicleResponseDto.builder()
        .vehicleId(vehicle.getId())
        .vehicleNumber(vehicle.getVehicleNumber())
        .model(vehicle.getModel())
        .capacity(vehicle.getCapacity())
        .status(vehicle.getStatus())
        .owner(mapOwnerToDto(vehicle.getOwner()))
        .vehicleCode(vehicle.getVehicleCode())
        .driverName(vehicle.getDriverName())
        .driverLicenceNo(vehicle.getDriverLicenceNo())
        .driverPermitNo(vehicle.getDriverPermitNo())
        .vehicleExpNo(vehicle.getVehicleExpNo())
        .branch(mapToBranchDto(vehicle.getBranch()))
        .type(vehicle.getType())
        .build();
  }

  private OwnerResponceDtos mapOwnerToDto(Owner owner) {
    if (owner == null)
      return null;
    return new OwnerResponceDtos(owner.getId(), owner.getOwnerName(), owner.getContactNumber(), owner.getAddress(),
        owner.getEmail());
  }

  private Route mapToEntity(RouteRequestDtos dto) {
    Party consigner = null;
    Party consignee = null;
    if (dto.getConsignerId() != null && dto.getConsigneeId() != null) {
      consigner = partyRepository.findById(dto.getConsignerId())
          .orElseThrow(() -> new EntityNotFoundException("Party not found with consignerId: " + dto.getConsignerId()));
      consignee = partyRepository.findById(dto.getConsigneeId())
          .orElseThrow(() -> new EntityNotFoundException("Party not found with consigneeId: " + dto.getConsigneeId()));
    }
    Vehicle vehicle = null;
    if (dto.getVehicleId() != null) {
      vehicle = vehicleRepository.findById(dto.getVehicleId())
          .orElseThrow(() -> new EntityNotFoundException("Vehicle not found with id: " + dto.getVehicleId()));
    }
    return Route.builder()
        .routeNumber(dto.getRouteNumber())
        .routeName(dto.getRouteName())
        .startLocation(dto.getStartLocation())
        .endLocation(dto.getEndLocation())
        .type(dto.getRouteType())
        .consignerId(consigner)
        .consigneeId(consignee)
        .vehicle(vehicle)
        .build();
  }

  @Override
  public RouteResponseDto createRoute(RouteRequestDtos routeRequestDtos) {
    try {
      if (routeRepository.existsByRouteNumberContainingIgnoreCase(routeRequestDtos.getRouteNumber()))
        throw new RuntimeException("This route number is already in use!!");
      if (routeRequestDtos.getConsignerId().equals(routeRequestDtos.getConsigneeId())) {
        throw new RuntimeException("You are passing same consignerId and ConsigneeId");
      }
      Route route = mapToEntity(routeRequestDtos);
      return mapToDto(routeRepository.save(route));
    } catch (DataIntegrityViolationException e) {
      throw new RuntimeException("This route number is already in use!!!");
    }
  }

  @Override
  public RouteResponseDto getRouteById(UUID id) {
    Route route = routeRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Route not found with id: " + id));
    return mapToDto(route);
  }

  @Override
  public Page<RouteResponseDto> getAllRoutes(Pageable pageable) {
    Page<Route> routes = routeRepository.findAll(pageable);
    return routes
        .map(this::mapToDto);
  }

  @Override
  public List<RouteResponseDto> searchRoutes(String name) {
    List<Route> routes = routeRepository.findByRouteNameContainingIgnoreCase(name);
    return routes.stream()
        .map(this::mapToDto)
        .collect(Collectors.toList());
  }

  @Override
  public RouteResponseDto updateRoute(UUID id, RouteRequestDtos routeRequestDtos) {
    try {
      Route existingRoute = routeRepository.findById(id)
          .orElseThrow(() -> new EntityNotFoundException("Route not found with id: " + id));

      if (!existingRoute.getRouteNumber().equals(routeRequestDtos.getRouteNumber()) &&
          routeRepository.existsByRouteNumberContainingIgnoreCase(routeRequestDtos.getRouteNumber())) {
        throw new RuntimeException("This route number is already in use!!!");
      }
      if (routeRequestDtos.getConsignerId().equals(routeRequestDtos.getConsigneeId())) {
        throw new RuntimeException("You are passing same consignerId and ConsigneeId");
      }
      existingRoute.setRouteNumber(routeRequestDtos.getRouteNumber());
      existingRoute.setRouteName(routeRequestDtos.getRouteName());
      existingRoute.setStartLocation(routeRequestDtos.getStartLocation());
      existingRoute.setEndLocation(routeRequestDtos.getEndLocation());
      existingRoute.setType(routeRequestDtos.getRouteType());
      if (routeRequestDtos.getConsignerId() != null && routeRequestDtos.getConsigneeId() != null) {
        Party consigner = partyRepository.findById(routeRequestDtos.getConsignerId())
            .orElseThrow(
                () -> new EntityNotFoundException(
                    "Party not found with consignerId: " + routeRequestDtos.getConsignerId()));
        Party consignee = partyRepository.findById(routeRequestDtos.getConsigneeId())
            .orElseThrow(
                () -> new EntityNotFoundException(
                    "Party not found with consigneeId: " + routeRequestDtos.getConsigneeId()));
        existingRoute.setConsignerId(consigner);
        existingRoute.setConsigneeId(consignee);
      }
      if (routeRequestDtos.getVehicleId() != null) {
        Vehicle vehicle = vehicleRepository.findById(routeRequestDtos.getVehicleId())
            .orElseThrow(
                () -> new EntityNotFoundException("Vehicle not found with id: " + routeRequestDtos.getVehicleId()));
        existingRoute.setVehicle(vehicle);
      }

      Route updatedRoute = routeRepository.save(existingRoute);
      return mapToDto(updatedRoute);
    } catch (DataIntegrityViolationException e) {
      throw new RuntimeException("This route number is already in use!!!");
    }
  }

  @Override
  public void deleteRoute(UUID id) {
    Route route = routeRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Route not found with id: " + id));
    routeRepository.delete(route);
  }
}
