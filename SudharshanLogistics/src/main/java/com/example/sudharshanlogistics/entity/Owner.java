package com.example.sudharshanlogistics.entity;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "owner_table")
public class Owner {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private String ownerName;
  private String contactNumber;
  private String address;
  private String email;

}
