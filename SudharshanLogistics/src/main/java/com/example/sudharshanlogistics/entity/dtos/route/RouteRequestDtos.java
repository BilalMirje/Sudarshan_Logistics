package com.example.sudharshanlogistics.entity.dtos.route;

import java.util.UUID;

import com.example.sudharshanlogistics.entity.Route.RouteType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RouteRequestDtos {

  private String routeNumber;
  private String routeName;
  private String startLocation;
  private String endLocation;
  private RouteType routeType;
  private UUID consignerId;
  private UUID consigneeId;
  private UUID vehicleId;

}
