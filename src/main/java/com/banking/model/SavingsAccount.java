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
    public String getAccountType(){
        return "Savings";
    }
    public double getMinBalanceForInterest(){
        return minBalanceForInterest;
    }
    public static class Builder extends Account.Builder<Builder>{
        private double minBalanceForInterest=1000;
        public Builder(String accountId,String holderName){
            super(accountId,holderName);
        }
        public Builder minBalanceForInterest(double minBalanceForInterest){
            this.minBalanceForInterest=minBalanceForInterest;
            return self();
        }
        @Override
        protected Builder self(){
            return this;
        }
        @Override
        public SavingsAccount build(){
            return new SavingsAccount(this);
        }
    }
}
