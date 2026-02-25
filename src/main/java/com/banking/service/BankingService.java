package com.banking.service;

import com.banking.exception.AccountNotFoundException;
import com.banking.exception.InsufficientFundsException;
import com.banking.exception.InvalidTransactionException;
import com.banking.factory.AccountFactory;
import com.banking.factory.AccountType;
import com.banking.model.Account;
import com.banking.model.Transaction;
import com.banking.observer.TransactionObserver;
import com.banking.repository.AccountRepository;
import com.banking.strategy.InterestStrategy;

import java.util.ArrayList;
import java.util.List;

public class BankingService {
    private final AccountRepository repository;
    private final List<TransactionObserver> observers = new ArrayList<>();
    private InterestStrategy interestStrategy;

    public BankingService(AccountRepository repository) {
        this.repository = repository;
    }

    public void addObserver(TransactionObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(TransactionObserver observer) {
        observers.remove(observer);
    }

    public void setInterestStrategy(InterestStrategy strategy) {
        this.interestStrategy = strategy;
    }

    public Account createAccount(AccountType type, String accountId,
                                 String holderName, double initialAmount) {
        if (repository.exists(accountId)) {
            throw new InvalidTransactionException("Account already exists: " + accountId);
        }
        Account account = AccountFactory.createAccount(type, accountId, holderName, initialAmount);
        repository.save(account);
        return account;
    }

    public void deposit(String accountId, double amount) {
        Account account = getAccountOrThrow(accountId);
        try {
            account.deposit(amount);
            notifyObservers(account, getLastTransaction(account));
        } catch (IllegalArgumentException e) {
            throw new InvalidTransactionException(e.getMessage());
        }
    }

    public void withdraw(String accountId, double amount) {
        Account account = getAccountOrThrow(accountId);
        try {
            account.withdraw(amount);
            notifyObservers(account, getLastTransaction(account));
        } catch (IllegalArgumentException e) {
            throw new InsufficientFundsException(accountId, amount, account.getBalance());
        }
    }

    public void transfer(String fromAccountId, String toAccountId, double amount) {
        Account from = getAccountOrThrow(fromAccountId);
        Account to = getAccountOrThrow(toAccountId);

        if (fromAccountId.equals(toAccountId)) {
            throw new InvalidTransactionException("Cannot transfer to the same account");
        }

        try {
            from.withdraw(amount);
            to.deposit(amount);
            notifyObservers(from, getLastTransaction(from));
            notifyObservers(to, getLastTransaction(to));
        } catch (IllegalArgumentException e) {
            throw new InsufficientFundsException(fromAccountId, amount, from.getBalance());
        }
    }

    public double calculateInterest(String accountId) {
        Account account = getAccountOrThrow(accountId);

        if (interestStrategy != null) {
            double interest = interestStrategy.calculateInterest(
                    account.getBalance(), account.getInterestRate());
            return interest;
        }

        return account.calculateInterest();
    }

    public Account getAccount(String accountId) {
        return getAccountOrThrow(accountId);
    }

    public List<Account> getAllAccounts() {
        return repository.findAll();
    }

    public List<Transaction> getTransactionHistory(String accountId) {
        Account account = getAccountOrThrow(accountId);
        return account.getTransactions();
    }

    private Account getAccountOrThrow(String accountId) {
        return repository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));
    }

    private Transaction getLastTransaction(Account account) {
        List<Transaction> transactions = account.getTransactions();
        return transactions.get(transactions.size() - 1);
    }

    private void notifyObservers(Account account, Transaction transaction) {
        for (TransactionObserver observer : observers) {
            observer.onTransaction(account, transaction);
        }
    }
}
