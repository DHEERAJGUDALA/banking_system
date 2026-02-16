package com.banking.model;

import java.time.LocalDateTime;

public class Transaction {
private final String transactionId;
private final TransactionType type;
private final double amount;
private final String accountId;
private final double balanceAfter;
private final LocalDateTime timestamp;
private final String description;

public Transaction(String transactionId, TransactionType type , double amount, String accountId, double balanceAfter, String description){
    this.transactionId=transactionId;
    this.type=type;
    this.amount=amount;
    this.accountId=accountId;
    this.balanceAfter=balanceAfter;
    this.timestamp=LocalDateTime.now();
    this.description=description;
}

public String getTransactionId(){
    return transactionId;
}

public TransactionType getType() {
    return type;
}
public double getAmount(){
    return amount;
}

public String getAccountId(){
    return accountId;
}

public double getBalanceAfter(){
    return balanceAfter;
}

public LocalDateTime getTimestamp(){
    return timestamp;
}

public String getDescription(){
    return description;
}

@Override
    public String toString(){
    return String.format("[%s] %s:$%.2f|Balance: $%.2f|%s",
            timestamp.toString().substring(0,19),
            type.getDisplayName(),
            amount,
            balanceAfter,
            description);
}

}
