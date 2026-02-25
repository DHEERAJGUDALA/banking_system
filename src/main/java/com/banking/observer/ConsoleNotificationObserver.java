package com.banking.observer;

import com.banking.model.Account;
import com.banking.model.Transaction;

public class ConsoleNotificationObserver implements TransactionObserver {

    @Override
    public void onTransaction(Account account, Transaction transaction) {
        System.out.printf("[NOTIFICATION] %s - %s: $%.2f | Balance: $%.2f%n",
                account.getHolderName(),
                transaction.getType().getDisplayName(),
                transaction.getAmount(),
                transaction.getBalanceAfter());
    }
}
