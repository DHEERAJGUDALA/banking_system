package com.banking.observer;

import com.banking.model.Account;
import com.banking.model.Transaction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AuditLogObserver implements TransactionObserver {
    private final List<String> auditLog = new ArrayList<>();

    @Override
    public void onTransaction(Account account, Transaction transaction) {
        String logEntry = String.format("[%s] Account: %s | %s | Amount: $%.2f | Balance: $%.2f",
                LocalDateTime.now().toString().substring(0, 19),
                account.getAccountId(),
                transaction.getType().getDisplayName(),
                transaction.getAmount(),
                transaction.getBalanceAfter());
        auditLog.add(logEntry);
    }

    public List<String> getAuditLog() {
        return Collections.unmodifiableList(auditLog);
    }

    public void printAuditLog() {
        System.out.println("\n===== AUDIT LOG =====");
        auditLog.forEach(System.out::println);
        System.out.println("=====================");
    }
}
