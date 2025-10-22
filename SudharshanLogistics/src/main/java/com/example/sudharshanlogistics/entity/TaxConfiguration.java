package com.example.sudharshanlogistics.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "tax_configuration")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class TaxConfiguration extends Audit {
    @Id
    @GeneratedValue(generator = "UUID")
    @UuidGenerator
    private UUID id;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal cgstPercent;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal sgstPercent;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal igstPercent;

    @Column(nullable = false)
    private LocalDate effectiveFrom;
}
