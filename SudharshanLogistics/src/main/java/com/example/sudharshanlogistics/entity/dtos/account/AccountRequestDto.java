package com.example.sudharshanlogistics.entity.dtos.account;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountRequestDto {
    @NotBlank(message = "Account number is required")
    private String accountNo;

    @NotBlank(message = "Account name is required")
    private String accountName;

    // Optional, default handled in service
    private String accountType;

    private String balanceMark;

    @NotNull(message = "Opening balance is required")
    private Double openingBalance;

    private UUID partyId;
}
