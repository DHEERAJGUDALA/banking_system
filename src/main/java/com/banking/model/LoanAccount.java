package com.banking.model;

public class LoanAccount extends Account {
private final double originalLoanAmount;
private LoanAccount(Builder builder){
    super(builder);
    this.originalLoanAmount=builder.originalLoanAmount;
}
@Override
}
