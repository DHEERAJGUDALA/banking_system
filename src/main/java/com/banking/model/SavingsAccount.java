package com.banking.model;

public class SavingsAccount extends Account {
    private final double minBalanceForInterest;
    private SavingsAccount(Builder builder){
        super(builder);
        this.minBalanceForInterest=builder.minBalanceForInterest;
    }
    @Override
    public double calculateInterest(){
        if(getBalance()>=minBalanceForInterest){
            double interest = getBalance()*getInterestRate();
            deposit(interest);
            return interest;
        }
        return 0;
    }
    @Override
}
