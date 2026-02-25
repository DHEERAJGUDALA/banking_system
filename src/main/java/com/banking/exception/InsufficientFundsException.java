package com.banking.exception;

public class InsufficientFundsException extends RuntimeException {
    private final String accountId;
    private final double requested;
    private final double available;

    public InsufficientFundsException(String accountId, double requested, double available) {
        super(String.format("Insufficient funds in account %s. Requested: $%.2f, Available: $%.2f",
                accountId, requested, available));
        this.accountId = accountId;
        this.requested = requested;
        this.available = available;
    }

    public String getAccountId() { return accountId; }
    public double getRequested() { return requested; }
    public double getAvailable() { return available; }
}
