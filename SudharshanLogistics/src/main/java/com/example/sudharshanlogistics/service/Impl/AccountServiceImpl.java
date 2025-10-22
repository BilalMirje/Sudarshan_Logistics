package com.example.sudharshanlogistics.service.Impl;

import com.example.sudharshanlogistics.entity.Account;
import com.example.sudharshanlogistics.entity.Party;
import com.example.sudharshanlogistics.entity.dtos.account.AccountRequestDto;
import com.example.sudharshanlogistics.entity.dtos.account.AccountResponseDto;
import com.example.sudharshanlogistics.entity.dtos.branch.BranchResponceDto;
import com.example.sudharshanlogistics.entity.dtos.party.PartyResponseDto;
import com.example.sudharshanlogistics.repository.AccountRepository;
import com.example.sudharshanlogistics.repository.PartyRepository;
import com.example.sudharshanlogistics.service.AccountService;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final PartyRepository partyRepository;
    private final ModelMapper modelMapper;

    @Override
    public AccountResponseDto createAccount(AccountRequestDto accountRequestDto) throws Exception {
        if (accountRepository.existsByAccountNo(accountRequestDto.getAccountNo())) {
            throw new Exception("Account Number already exists!");
        }

        if (accountRequestDto.getPartyId() != null) {
            Optional<Party> party = partyRepository.findById(accountRequestDto.getPartyId());
            if (!party.isPresent()) {
                throw new Exception("Party not found with ID: " + accountRequestDto.getPartyId());
            }
        }

        Account account = mapRequestToEntity(accountRequestDto);
        if (accountRequestDto.getPartyId() != null) {
            account.setParty(partyRepository.findById(accountRequestDto.getPartyId()).get());
        }
        Account savedAccount = accountRepository.save(account);

        return mapEntityToResponse(savedAccount);
    }

    @Override
    public AccountResponseDto updateAccount(UUID accountId, AccountRequestDto accountRequestDto) throws Exception {
        Optional<Account> optionalAccount = accountRepository.findById(accountId);
        if (!optionalAccount.isPresent()) {
            throw new Exception("Account not found with ID: " + accountId);
        }

        Account existingAccount = optionalAccount.get();

        if (accountRequestDto.getAccountNo() != null)
            existingAccount.setAccountNo(accountRequestDto.getAccountNo());

        if (accountRequestDto.getAccountName() != null)
            existingAccount.setAccountName(accountRequestDto.getAccountName());

        if (accountRequestDto.getAccountType() != null) {
            existingAccount
                    .setAccountType(Account.AccountType.valueOf(accountRequestDto.getAccountType().toUpperCase()));
        }

        if (accountRequestDto.getBalanceMark() != null) {
            existingAccount
                    .setBalanceMark(Account.BalanceMark.valueOf(accountRequestDto.getBalanceMark().toUpperCase()));
        }

        if (accountRequestDto.getOpeningBalance() != null)
            existingAccount.setOpeningBalance(accountRequestDto.getOpeningBalance());

        if (accountRequestDto.getPartyId() != null) {
            Optional<Party> party = partyRepository.findById(accountRequestDto.getPartyId());
            if (!party.isPresent()) {
                throw new Exception("Party not found with ID: " + accountRequestDto.getPartyId());
            }
            existingAccount.setParty(party.get());
        }

        Account updatedAccount = accountRepository.save(existingAccount);
        return mapEntityToResponse(updatedAccount);
    }

    @Override
    public String deleteAccount(UUID accountId) throws Exception {
        Optional<Account> optionalAccount = accountRepository.findById(accountId);
        if (!optionalAccount.isPresent()) {
            throw new Exception("Account not found with ID: " + accountId);
        }
        accountRepository.deleteById(accountId);
        return "account delete successfully";
    }

    @Override
    public boolean existsByAccountId(UUID accountId) {
        return accountRepository.existsById(accountId);
    }

    @Override
    public Page<AccountResponseDto> getAllAccounts(Pageable pageable) {
        return accountRepository.findAll(pageable)
                .map(this::mapEntityToResponse);

    }

    @Override
    public List<AccountResponseDto> getAccountByNameOrNo(String accountName, String accountNo) {
        return accountRepository.findByAccountNameContainingIgnoreCaseOrAccountNoContaining(accountName, accountNo)
                .stream()
                .map(this::mapEntityToResponse)
                .collect(Collectors.toList());
    }

    private Account mapRequestToEntity(AccountRequestDto dto) {
        return Account.builder()
                .accountNo(dto.getAccountNo())
                .accountName(dto.getAccountName())
                .accountType(
                        dto.getAccountType() != null ? Account.AccountType.valueOf(dto.getAccountType().toUpperCase())
                                : Account.AccountType.CASH)
                .balanceMark(
                        dto.getBalanceMark() != null ? Account.BalanceMark.valueOf(dto.getBalanceMark().toUpperCase())
                                : Account.BalanceMark.CREDIT)
                .openingBalance(dto.getOpeningBalance())
                .build();
    }

    private AccountResponseDto mapEntityToResponse(Account account) {
        return AccountResponseDto.builder()
                .accountId(account.getAccountId())
                .accountName(account.getAccountName())
                .accountNo(account.getAccountNo())
                .accountType(account.getAccountType() != null ? account.getAccountType().name() : null)
                .balanceMark(account.getBalanceMark() != null ? account.getBalanceMark().name() : null)
                .openingBalance(account.getOpeningBalance())
                .party(mapPartyToResponse(account.getParty()))
                .build();
    }

    private PartyResponseDto mapPartyToResponse(Party party) {
        if (party == null)
            return null;
        return PartyResponseDto.builder()
                .id(party.getId())
                .partyCode(party.getPartyCode())
                .partyName(party.getPartyName())
                .partyNumber(party.getPartyNumber())
                .partyAddress(party.getPartyAddress())
                .gstNumber(party.getGstNumber())
                .district(party.getDistrict())
                .pinCode(party.getPinCode())
                .stateCode(party.getStateCode())
                .vendorMailId(party.getVendorMailId())
                .division(party.getDivision())
                .branchDto(
                        party.getBranch() != null ? modelMapper.map(party.getBranch(), BranchResponceDto.class) : null)
                .build();
    }
}
