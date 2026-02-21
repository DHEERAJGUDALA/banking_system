package com.banking.model;

public class SavingsAccount extends Account {
    private final double minBalanceForInterest;
    private SavingsAccount(Builder builder){
        super(builder);
        this.minBalanceForInterest=builder.minBalanceForInterest;
    }
    @Override
    public double calculateInterest(){
        if(getBalance()>=minBlanaceForInterest){
            double interest = getBalance()*getInterestRate();

        }
    }
}
