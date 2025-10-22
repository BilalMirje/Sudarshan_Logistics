package com.example.sudharshanlogistics.entity.dtos.route;

import java.util.UUID;

import com.example.sudharshanlogistics.entity.dtos.party.PartyResponseDto;
import com.example.sudharshanlogistics.entity.dtos.vehicle.VehicleResponseDto;
import com.example.sudharshanlogistics.entity.Route.RouteType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RouteResponseDto {

  private UUID id;
  private String routeNumber;
  private String routeName;
  private String startLocation;
  private String endLocation;
  private RouteType type;

  private PartyResponseDto consignerId;
  private PartyResponseDto consigneeId;
  private VehicleResponseDto vehicle;

}
