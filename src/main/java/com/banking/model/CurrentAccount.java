package com.banking.model;

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
    public double calculateInterest(){
    return 0;
}
@Override
    public String getAccountType(){
    return "Current";
}
public double getOverdraftLimit(){
    return overdraftLimit;
}

public static class Builder extends Account.Builder<Builder>{
    private double overdraftLimit=10000;
    public Builder(String accountId,String holderName){
        super(accountId,holderName);
    }
    public Builder overdraftLimit(double overdraftLimit){
        this.overdraftLimit=overdraftLimit;
        return self();
    }
    @Override
    protected Builder self(){
        return this;
    }
    @Override
    public CurrentAccount build(){
        return new CurrentAccount(this);
    }
  }
 }
