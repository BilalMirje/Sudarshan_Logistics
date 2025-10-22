package com.example.sudharshanlogistics.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.sudharshanlogistics.entity.Account;

import java.util.List;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {
    boolean existsByAccountNo(String accountNo);

    List<Account> findByAccountNameContainingIgnoreCaseOrAccountNoContaining(String accountName, String accountNo);

    boolean existsByParty(com.example.sudharshanlogistics.entity.Party party);
}
