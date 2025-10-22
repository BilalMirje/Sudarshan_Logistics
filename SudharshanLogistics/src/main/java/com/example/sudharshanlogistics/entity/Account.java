package com.example.sudharshanlogistics.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class Account extends Audit {
    @Id
    @GeneratedValue(generator = "UUID")
    @UuidGenerator
    private UUID accountId;

    @Column(unique = true)
    private String accountNo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType accountType;

    private String accountName;

    private Double openingBalance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BalanceMark balanceMark;

    @ManyToOne
    @JoinColumn(name = "party_id")
    private Party party;

    public enum AccountType {
        CASH,
        BANK,
        PARTY,
        CREDITOR,
        DEBITOR,
        JOURNAL
    }

    public enum BalanceMark {
        CREDIT,
        DEBIT
    }

}
