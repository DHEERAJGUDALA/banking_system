package com.banking.observer;

import com.banking.model.Account;
import com.banking.model.Transaction;
import com.banking.model.TransactionType;

public class FraudDetectionObserver implements TransactionObserver {
    private final double largeTransactionThreshold;

    public FraudDetectionObserver(double threshold) {
        this.largeTransactionThreshold = threshold;
    }

    @Override
    public void onTransaction(Account account, Transaction transaction) {
        if (transaction.getAmount() > largeTransactionThreshold) {
            System.out.printf("[FRAUD ALERT] Large %s of $%.2f on account %s (%s)%n",
                    transaction.getType().getDisplayName().toLowerCase(),
                    transaction.getAmount(),
                    account.getAccountId(),
                    account.getHolderName());
        }

        if (transaction.getType() == TransactionType.WITHDRAWAL
                && transaction.getAmount() > account.getBalance() * 0.9) {
            System.out.printf("[FRAUD ALERT] Near-total withdrawal of $%.2f from account %s (Balance was: $%.2f)%n",
                    transaction.getAmount(),
                    account.getAccountId(),
                    transaction.getBalanceAfter() + transaction.getAmount());
        }
    }
}
