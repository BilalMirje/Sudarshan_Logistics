package com.example.sudharshanlogistics.entity.dtos.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
import com.example.sudharshanlogistics.entity.dtos.party.PartyResponseDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountResponseDto {
    private UUID accountId;
    private String accountNo;
    private String accountType;
    private String accountName;
    private Double openingBalance;
    private String balanceMark;
    private PartyResponseDto party;
}
