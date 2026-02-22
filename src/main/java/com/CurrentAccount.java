package com;

public class CurrentAccount extends Account{
private final double overdraftLimit;
private CurrentAccount(Builder builder){
    super(builder);
    this.overdraftLimit=builder.overdraftLimit;
}
@Override
}
