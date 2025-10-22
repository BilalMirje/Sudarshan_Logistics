package com.example.sudharshanlogistics.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.sudharshanlogistics.entity.dtos.account.AccountRequestDto;
import com.example.sudharshanlogistics.entity.dtos.account.AccountResponseDto;

import java.util.List;
import java.util.UUID;

public interface AccountService {
    AccountResponseDto createAccount(AccountRequestDto accountRequestDto) throws Exception;

    AccountResponseDto updateAccount(UUID accountId, AccountRequestDto accountRequestDto) throws Exception;

    String deleteAccount(UUID accountId) throws Exception;

    boolean existsByAccountId(UUID accountId);

    public Page<AccountResponseDto> getAllAccounts(Pageable pageable);

    List<AccountResponseDto> getAccountByNameOrNo(String accountName, String accountNo);
}
