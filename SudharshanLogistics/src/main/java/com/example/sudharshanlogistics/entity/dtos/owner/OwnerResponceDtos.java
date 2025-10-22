package com.example.sudharshanlogistics.entity.dtos.owner;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OwnerResponceDtos {

  private UUID ownerId;
  private String ownerName;
  private String contactNumber;
  private String address;
  private String email;

}
