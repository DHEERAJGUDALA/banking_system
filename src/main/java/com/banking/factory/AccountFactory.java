package com.banking.factory;

import com.banking.model.Account;
import com.banking.model.CurrentAccount;
import com.banking.model.LoanAccount;
import com.banking.model.SavingsAccount;

public class AccountFactory {
    private static final double DEFAULT_SAVINGS_INTEREST=0.04;
    private static final double DEFAULT_LOAN_INTEREST=0.08;
    private static final double DEFAULT_OVERDRAFT_LIMIT=50000;

    public static Account createAccount(AccountType type, String accountId, String holderName,double initialAmount){
        return switch(type){
            case SAVINGS -> new SavingsAccount.Builder(accountId,holderName)
                    .balance(initialAmount)
                    .interestRate(DEFAULT_SAVINGS_INTEREST)
                    .build();
            case CURRENT -> new CurrentAccount.Builder(accountId,holderName)
                    .balance(initialAmount)
                    .overdraftLimit(DEFAULT_OVERDRAFT_LIMIT)
                    .build();
            case LOAN -> new LoanAccount.Builder(accountId,holderName,initialAmount)
                    .interestRate(DEFAULT_LOAN_INTEREST)
                    .build();
        };
    }
}
