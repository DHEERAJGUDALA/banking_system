package com.banking.repository;

import com.banking.model.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepository {
    void save(Account account);
    Optional<Account> findById(String accountId);
    List<Account> findAll();
    List<Account> findByHolderName(String holderName);
    void delete(String accountId);
    boolean exists(String accountId);
}
