package com;

public class CurrentAccount extends Account{
private final double overdraftLimit;
private CurrentAccount(Builder builder){
    super(builder);
    this.overdraftLimit=builder.overdraftLimit;
}
@Override
    public void withdraw(double amount){
    if(amount<=0){
        throw new IllegalArgumentException("Withdrawal amount must be positive");
    }
    if(amount>getBalance()+overdraftLimit){
        throw new IllegalArgumentException("Exceeds overdraft limit. Available: "+ (getBalance()+overdraftLimit));
    }
    setBalance(getBalance()-amount);
    recordTransaction(TransactionType.WITHDRAWAL,amount,"Withdrawal (Current)");
}
@Override
}
