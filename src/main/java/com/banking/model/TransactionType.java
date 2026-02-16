package com.banking.model;

public enum TransactionType {
    DEPOSIT("Deposit"),
    WITHDRAWAL("Withdrawal"),
    TRANSFER("Transfer"),
    INTEREST("Interest");

    private final String displayName;
    TransacionType(String displayName){
        this.displayName=displayName;
    }
    public String getDisplayName(){
        return displayName;
    }
}
