package com.banking.strategy;

public class SimpleInterestStrategy implements InterestStrategy {

    @Override
    public double calculateInterest(double balance, double rate) {
        return balance * rate;
    }

    @Override
    public String getStrategyName() {
        return "Simple Interest";
    }
}