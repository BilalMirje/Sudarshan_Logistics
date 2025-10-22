package com.example.sudharshanlogistics.entity;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "item_table")
@Builder
public class Item {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private String itemName;

  @Column(unique = true)
  private String partNumber;

  private Integer itemQuantity;

  private String itemDescription;

  private Integer quantityInBox;
  private Double weightPerQuantity;

  private BigDecimal rateOnBox;
  private BigDecimal rateOnWeight;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "route_id", nullable = false)
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private Route route;

  @ManyToOne
  @JoinColumn(name = "lr_invoice_id")
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private LrRecepit lrInvoice;

  @ManyToOne
  @JoinColumn(name = "lr_receipt_id")
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private LorryInvoice lorryInvoice;

}
