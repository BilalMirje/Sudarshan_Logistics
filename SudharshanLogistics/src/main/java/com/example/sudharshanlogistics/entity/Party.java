package com.example.sudharshanlogistics.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@Table(name = "party_table")
public class Party extends Audit {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private String partyCode;
  private String partyName;
  private String partyNumber;

  private String partyAddress;

  private String district;
  private String division;

  @Column(nullable = false)
  private String pinCode;

  @Column(nullable = false)
  private String stateCode;

  private String vendorMailId;

  @Column(unique = true)
  private String gstNumber;

  @ManyToOne
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private Branch branch;

}
