package com.example.sudharshanlogistics.entity.dtos.item;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemRequestDto {

  private String itemName;
  private String partNumber;
  private Integer quantityInBox;
  private Double weightPerQuantity;
  private BigDecimal rateOnBox;
  private BigDecimal rateOnWeight;
  private Integer itemQuantity;
  private String itemDescription;
  private UUID routeId;

}
