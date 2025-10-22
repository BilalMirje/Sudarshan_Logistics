package com.example.sudharshanlogistics.entity.dtos.item;

import java.math.BigDecimal;
import java.util.UUID;

import com.example.sudharshanlogistics.entity.dtos.route.RouteResponseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemResponseDto {

  private UUID id;
  private String itemName;
  private String partNumber;
  private Integer quantityInBox;
  private Double weightPerQuantity;
  private BigDecimal rateOnBox;
  private BigDecimal rateOnWeight;
  private String itemDescription;
  private Integer itemQuantity;
  private RouteResponseDto route;

}
