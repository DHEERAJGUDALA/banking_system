package com.banking.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Account {
    private final String accountId;
    private final String holderName;
    private double balance;
    private final double interestRate;
    private final double minimumBalance;
    private boolean active;
    private final List<Transaction> transactions = new ArrayList<>();

    protected Account(Builder<?> builder){
        this.accountId = builder.accountId;
        this.holderName = builder.holderName;
        this.balance = builder.balance;
        this.interestRate = builder.interestRate;
        this.minimumBalance = builder.minimumBalance;
        this.active = builder.active;
    }
    public void deposit(double amount){
        if(amount<=0){
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        this.balance+=amount;
        recordTransaction(TransactionType.DEPOSIT , amount , "Deposit");
    }
    public void withdraw(double amount){
        if(amount<=0){
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        if(amount>balance){
            throw new IllegalArgumentException("Insufficient funds");
        }
        if(balance-amount<minimumBalance){
            throw new IllegalArgumentException("Balance cannot go below minimum :"+minimumBalance);
        }
        this.balance-=amount;
        recordTransaction(TransactionType.WITHDRAWAL,amount,"Withdrawal");
    }

    public abstract double calculateInterest();
    public abstract String getAccountType();
    protected void recordTransaction(TransactionType type, double amount,String description){
        String txnId="TXN"+System.currentTimeMillis();
        Transaction txn=new Transaction(txnId,type,amount,accountId,balance,description);
        transactions.add(txn);
    }

    public String getAccountId(){
        return accountId;
    }
    public String getHolderName(){
        return holderName;
    }
    public double getBalance() { return balance; }
    public double getInterestRate(){
        return interestRate;
    }
    public boolean isActive(){
        return active;
    }
    public void setActive(boolean active){
        this.active = active;
    }

    protected void setBalance(double balance){
        this.balance=balance;
    }
    public List<Transaction> getTransactions(){
        return Collections.unmodifiableList(transactions);
    }

    @Override
    public String toString(){
        return String.format("%s [%s] %s | Balance: $%.2f | Rate: %.1f%%",getAccountType(),accountId,holderName,balance,interestRate*100);
    }

    public abstract static class Builder<T extends Builder<T>>{
        private final String accountId;
        private final String holderName;
        private double balance=0;
        private double interestRate=0;
        private double minimumBalance=0;
        private boolean active=true;

        public Builder(String accountId,String holderName){
            this.accountId=accountId;
            this.holderName=holderName;
        }
        public T balance(double balance){
            this.balance=balance;
            return self();
        }
        public T interestRate(double interestRate){
            this.interestRate = interestRate;
            return self();
        }
        public T minimumBalance(double minimumBalance){
            this.minimumBalance = minimumBalance;
            return self();
        }
        public T active(boolean active){
            this.active = active;
            return self();
        }
        protected abstract T self();
        public abstract Account build();
    }
}
