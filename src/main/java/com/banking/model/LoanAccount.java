package com.banking.model;

public class LoanAccount extends Account {
private final double originalLoanAmount;
private LoanAccount(Builder builder){
    super(builder);
    this.originalLoanAmount=builder.originalLoanAmount;
}
@Override
    public void deposit(double amount){
    if(amount<=0){
        throw new IllegalArgumentException("Payment amount must be positive");
    }
    if(amount>getBalance()){
        throw new IllegalArgumentException("Payment exceeds remaining loan balance");
    }
    setBalance(getBalance()-amount);
    recordTransaction(TransactionType.DEPOSIT,amount,"Loan Payment");
}
@Override
    public double calculateInterest(){
    if(getBalance()>0){
        double interest=getBalance()*getInterestRate();
        setBalance(getBalance()+interest);
        recordTransaction(TransactionType.INTEREST,interest,"Loan Interest Charged");
        return interest;
    }
    return 0;
}
@Override
}
