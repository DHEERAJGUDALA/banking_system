package com.banking.strategy;

public class CompoundInterestStrategy implements InterestStrategy {
    private final int compoundingPeriods;

    public CompoundInterestStrategy(int compoundingPeriods) {
        this.compoundingPeriods = compoundingPeriods;
    }

    @Override
    public double calculateInterest(double balance, double rate) {
        double ratePerPeriod = rate / compoundingPeriods;
        double compoundedAmount = balance * Math.pow(1 + ratePerPeriod, compoundingPeriods);
        return compoundedAmount - balance;
    }

    @Override
    public String getStrategyName() {
        return "Compound Interest (" + compoundingPeriods + "x/year)";
    }
}