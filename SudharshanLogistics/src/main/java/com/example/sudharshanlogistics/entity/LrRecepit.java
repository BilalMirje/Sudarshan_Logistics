package com.example.sudharshanlogistics.entity;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Lr_Invoice")
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
public class LrRecepit extends Audit {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(unique = true)
  private String cNoteNo;

  @Column(nullable = false)
  private LocalDate date;

  @Enumerated(EnumType.STRING)
  private PaymentMode paymentMode;

  private boolean insured;

  @OneToMany(mappedBy = "lrInvoice")
  private List<Item> items;

  private double doorDeliveryCharge;
  private double doorPickupCharge;
  private double hamaliCharge;
  private double statisticalCharge;
  private double serviceCharge;
  private double totalFreight;

  private String invoiceNo;
  private double invoiceValue;
  private String amountInWords;

  private String transportId;

  @ManyToOne
  private Branch branch;

  @ManyToOne
  private Party party;

  @ManyToOne
  private Party consignee;

  public enum PaymentMode {
    CASH,
    TO_PAY,
    CREDIT
  }
}
