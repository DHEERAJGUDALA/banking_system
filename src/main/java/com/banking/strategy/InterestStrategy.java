package com.banking.strategy;

public interface InterestStrategy {
    double calculateInterest(double balance,double rate);
    String getStrategyName();
}
