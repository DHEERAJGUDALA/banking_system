package com.banking;

import com.banking.factory.AccountType;
import com.banking.model.Account;
import com.banking.observer.AuditLogObserver;
import com.banking.observer.ConsoleNotificationObserver;
import com.banking.observer.FraudDetectionObserver;
import com.banking.repository.InMemoryAccountRepository;
import com.banking.service.BankingService;
import com.banking.strategy.CompoundInterestStrategy;
import com.banking.strategy.SimpleInterestStrategy;
import com.banking.strategy.TieredInterestStrategy;

public class Main {
    public static void main(String[] args) {
        InMemoryAccountRepository repository = new InMemoryAccountRepository();
        BankingService service = new BankingService(repository);

        // Set up observers
        ConsoleNotificationObserver notifier = new ConsoleNotificationObserver();
        AuditLogObserver auditLog = new AuditLogObserver();
        FraudDetectionObserver fraudDetector = new FraudDetectionObserver(100000);

        service.addObserver(notifier);
        service.addObserver(auditLog);
        service.addObserver(fraudDetector);

        System.out.println("========== BANKING SYSTEM ==========\n");

        // --- Create Accounts ---
        System.out.println("--- Creating Accounts ---");
        Account savings = service.createAccount(AccountType.SAVINGS, "SA001", "Alice", 50000);
        Account current = service.createAccount(AccountType.CURRENT, "CA001", "Bob", 100000);
        Account loan = service.createAccount(AccountType.LOAN, "LA001", "Charlie", 500000);

        System.out.println(savings);
        System.out.println(current);
        System.out.println(loan);

        // --- Deposits ---
        System.out.println("\n--- Deposits ---");
        service.deposit("SA001", 25000);
        service.deposit("CA001", 50000);
        service.deposit("LA001", 20000);  // Loan payment

        // --- Withdrawals ---
        System.out.println("\n--- Withdrawals ---");
        service.withdraw("SA001", 10000);
        service.withdraw("CA001", 180000);  // Uses overdraft

        // --- Transfer ---
        System.out.println("\n--- Transfer ---");
        service.transfer("SA001", "CA001", 15000);

        // --- Large transaction (triggers fraud alert) ---
        System.out.println("\n--- Large Transaction ---");
        service.deposit("CA001", 200000);

        // --- Interest Calculation ---
        System.out.println("\n--- Interest (Using Account's Own Method) ---");
        System.out.println("Savings Interest: $" + String.format("%.2f", service.calculateInterest("SA001")));
        System.out.println("Loan Interest: $" + String.format("%.2f", service.calculateInterest("LA001")));

        // --- Strategy Pattern Demo ---
        System.out.println("\n--- Interest Strategy: Simple ---");
        service.setInterestStrategy(new SimpleInterestStrategy());
        double simpleInterest = service.calculateInterest("SA001");
        System.out.println("Simple Interest on SA001: $" + String.format("%.2f", simpleInterest));

        System.out.println("\n--- Interest Strategy: Compound (12x/year) ---");
        service.setInterestStrategy(new CompoundInterestStrategy(12));
        double compoundInterest = service.calculateInterest("SA001");
        System.out.println("Compound Interest on SA001: $" + String.format("%.2f", compoundInterest));

        System.out.println("\n--- Interest Strategy: Tiered ---");
        service.setInterestStrategy(new TieredInterestStrategy(100000, 0.03, 500000, 0.04, 0.05));
        double tieredInterest = service.calculateInterest("SA001");
        System.out.println("Tiered Interest on SA001: $" + String.format("%.2f", tieredInterest));

        // --- Account Balances ---
        System.out.println("\n--- Final Account States ---");
        service.getAllAccounts().forEach(System.out::println);

        // --- Transaction History ---
        System.out.println("\n--- Transaction History (SA001) ---");
        service.getTransactionHistory("SA001").forEach(System.out::println);

        // --- Audit Log ---
        auditLog.printAuditLog();

        // --- Error Handling Demo ---
        System.out.println("\n--- Error Handling ---");
        try {
            service.withdraw("SA001", 999999);
        } catch (Exception e) {
            System.out.println("Caught: " + e.getMessage());
        }

        try {
            service.getAccount("INVALID");
        } catch (Exception e) {
            System.out.println("Caught: " + e.getMessage());
        }

        try {
            service.deposit("LA001", 999999);
        } catch (Exception e) {
            System.out.println("Caught: " + e.getMessage());
        }

        System.out.println("\n========== DONE ==========");
    }
}
