package com.example.sudharshanlogistics.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class LorryInvoice extends Audit {

    @Id
    @GeneratedValue(generator = "UUID")
    @UuidGenerator
    private UUID id;

    @Column(unique = true, nullable = false)
    private String wayBillNo;

    @Column(nullable = false)
    private LocalDate date;

    @Transient
    private List<Item> items = new ArrayList<>();

    public void addItem(Item item) {
        this.items.add(item);
        item.setLorryInvoice(this);
    }

    public void removeItem(Item item) {
        if (this.items != null) {
            this.items.remove(item);
        }
        item.setLorryInvoice(null);
    }

    private String collType;
    private String delvType;
    private String gcType;

    @Enumerated(EnumType.STRING)
    private ModeOfPayment modeOfPayment;

    private String freightPayBy;
    private Double actualWeight;
    private BigDecimal ratePerKg;
    private Integer totalPackages;
    private BigDecimal invoiceValue;
    private BigDecimal subTotal;
    private BigDecimal igstTax;
    private BigDecimal cgstTax;
    private BigDecimal sgstTax;
    private BigDecimal grandTotal;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Status status = Status.CREATED;

    public enum ModeOfPayment {
        CASH, CREDIT, TO_PAY
    }

    public enum Status {
        CREATED, IN_TRANSIT, DELIVERED
    }
}
