package com.banking.strategy;

public class TieredInterestStrategy implements InterestStrategy {
    private final double tier1Limit;
    private final double tier1Rate;
    private final double tier2Limit;
    private final double tier2Rate;
    private final double tier3Rate;

    public TieredInterestStrategy(double tier1Limit, double tier1Rate,double tier2Limit, double tier2Rate,double tier3Rate) {
        this.tier1Limit = tier1Limit;
        this.tier1Rate = tier1Rate;
        this.tier2Limit = tier2Limit;
        this.tier2Rate = tier2Rate;
        this.tier3Rate = tier3Rate;
    }

    @Override
    public double calculateInterest(double balance, double rate) {
        double interest = 0;

        if (balance <= tier1Limit) {
            interest = balance * tier1Rate;
        } else if (balance <= tier2Limit) {
            interest = tier1Limit * tier1Rate;
            interest += (balance - tier1Limit) * tier2Rate;
        } else {
            interest = tier1Limit * tier1Rate;
            interest += (tier2Limit - tier1Limit) * tier2Rate;
            interest += (balance - tier2Limit) * tier3Rate;
        }

        return interest;
    }

    @Override
    public String getStrategyName() {
        return "Tiered Interest";
    }
}