package com.banking.repository;

import com.banking.model.Account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class InMemoryAccountRepository implements AccountRepository {
    private final Map<String, Account> accounts = new HashMap<>();

    @Override
    public void save(Account account) {
        accounts.put(account.getAccountId(), account);
    }

    @Override
    public Optional<Account> findById(String accountId) {
        return Optional.ofNullable(accounts.get(accountId));
    }

    @Override
    public List<Account> findAll() {
        return new ArrayList<>(accounts.values());
    }

    @Override
    public List<Account> findByHolderName(String holderName) {
        return accounts.values().stream()
                .filter(a -> a.getHolderName().equalsIgnoreCase(holderName))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(String accountId) {
        accounts.remove(accountId);
    }

    @Override
    public boolean exists(String accountId) {
        return accounts.containsKey(accountId);
    }
}
