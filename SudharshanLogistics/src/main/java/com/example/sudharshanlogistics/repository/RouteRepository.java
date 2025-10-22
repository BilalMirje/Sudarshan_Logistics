package com.example.sudharshanlogistics.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.sudharshanlogistics.entity.Route;

@Repository
public interface RouteRepository extends JpaRepository<Route, UUID> {

  Route findByRouteNumber(String routeNumber);

  boolean existsByRouteNumberContainingIgnoreCase(String routeNumber);

  List<Route> findByRouteNameContainingIgnoreCase(String name);

  // boolean existsByConsignerIdAndConsigneeId(UUID consignerId, UUID
  // consigneeId);

}
