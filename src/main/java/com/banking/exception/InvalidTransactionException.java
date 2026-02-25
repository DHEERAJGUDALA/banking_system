package com.banking.exception;

public class InvalidTransactionException extends RuntimeException {
    private final String reason;

    public InvalidTransactionException(String reason) {
        super("Invalid transaction: " + reason);
        this.reason = reason;
    }

    public String getReason() { return reason; }
}
