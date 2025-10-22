package com.example.sudharshanlogistics.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.sudharshanlogistics.entity.dtos.route.RouteRequestDtos;
import com.example.sudharshanlogistics.entity.dtos.route.RouteResponseDto;
import com.example.sudharshanlogistics.service.RouteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/route")
@RequiredArgsConstructor
@Tag(name = "Route Controller", description = "CRUD operations for Route entity")
@CrossOrigin("*")
public class RouteController {

  private final RouteService routeService;

  @PostMapping("/create-route")
  @Operation(summary = "Create Route", description = "Create a new route")
  public ResponseEntity<?> createRoute(@RequestBody RouteRequestDtos routeRequestDtos) {
    try {
      RouteResponseDto responseDto = routeService.createRoute(routeRequestDtos);
      return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body("This route number is already in use");
    }
  }

  @GetMapping("/get-route")
  @Operation(summary = "Get Route by ID", description = "Get route details by ID")
  public ResponseEntity<?> getRouteById(@RequestParam UUID id) {
    try {
      RouteResponseDto responseDto = routeService.getRouteById(id);
      return ResponseEntity.ok(responseDto);
    } catch (EntityNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }

  @GetMapping("/get-all-route")
  @Operation(summary = "Get All Routes", description = "Get list of all routes")
  public ResponseEntity<?> getAllRoutes(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "id") String sortBy,
      @RequestParam(defaultValue = "asc") String sortDir) {

    Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
        : Sort.by(sortBy).descending();
    Pageable pageable = PageRequest.of(page, size, sort);
    Page<RouteResponseDto> routes = routeService.getAllRoutes(pageable);
    return ResponseEntity.ok(routes);
  }

  @GetMapping("/search-route")
  @Operation(summary = "Search Routes", description = "Search routes by name")
  public ResponseEntity<?> searchRoutes(@RequestParam String name) {
    List<RouteResponseDto> routes = routeService.searchRoutes(name);
    return ResponseEntity.ok(routes);
  }

  @PutMapping("/update-route")
  @Operation(summary = "Update Route", description = "Update route details by ID")
  public ResponseEntity<?> updateRoute(@RequestParam UUID id, @RequestBody RouteRequestDtos routeRequestDtos) {
    try {
      RouteResponseDto responseDto = routeService.updateRoute(id, routeRequestDtos);
      return ResponseEntity.ok(responseDto);
    } catch (EntityNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body("This route number is already in use");
    }
  }

  @DeleteMapping("/delete-route")
  @Operation(summary = "Delete Route", description = "Delete route by ID")
  public ResponseEntity<?> deleteRoute(@RequestParam UUID id) {
    try {
      routeService.deleteRoute(id);
      return ResponseEntity.ok().build();
    } catch (EntityNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }
}
