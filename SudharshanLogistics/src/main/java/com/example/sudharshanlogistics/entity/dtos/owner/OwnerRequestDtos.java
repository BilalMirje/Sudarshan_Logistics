package com.example.sudharshanlogistics.entity.dtos.owner;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OwnerRequestDtos {

  private String ownerName;
  private String contactNumber;
  private String address;
  private String email;

}
