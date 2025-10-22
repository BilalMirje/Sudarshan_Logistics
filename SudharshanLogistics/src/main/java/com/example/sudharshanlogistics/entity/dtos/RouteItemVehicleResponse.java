package com.example.sudharshanlogistics.entity.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteItemVehicleResponse {
  private String startLocation;
  private String endLocation;
  private Integer itemQuantity;
  private String itemDescription;
  private String vehicleNo;
}
