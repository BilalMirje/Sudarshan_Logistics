package com.example.sudharshanlogistics.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.sudharshanlogistics.entity.dtos.route.RouteRequestDtos;
import com.example.sudharshanlogistics.entity.dtos.route.RouteResponseDto;
import com.example.sudharshanlogistics.entity.Route;

public interface RouteService {

  RouteResponseDto createRoute(RouteRequestDtos routeRequestDtos);

  RouteResponseDto getRouteById(UUID id);

  Page<RouteResponseDto> getAllRoutes(Pageable pageable);

  List<RouteResponseDto> searchRoutes(String name);

  RouteResponseDto updateRoute(UUID id, RouteRequestDtos routeRequestDtos);

  void deleteRoute(UUID id);

  RouteResponseDto mapToDto(Route route);
}
