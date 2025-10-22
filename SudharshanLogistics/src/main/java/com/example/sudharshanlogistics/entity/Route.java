package com.example.sudharshanlogistics.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
@Table(name = "route_table")
public class Route extends Audit {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;

  @Column(unique = true)
  private String routeNumber;
  private String routeName;
  private String startLocation;
  private String endLocation;

  @Enumerated(EnumType.STRING)
  private RouteType type;

  @ManyToOne
  @JoinColumn(name = "consigner_Id")
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private Party consignerId;

  @ManyToOne
  @JoinColumn(name = "consignee_Id")
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private Party consigneeId;

  @ManyToOne(fetch = FetchType.LAZY)
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private Vehicle vehicle;

  public enum RouteType {
    IN_STATE,
    OUT_STATE
  }

}
