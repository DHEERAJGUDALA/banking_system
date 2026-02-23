package com.banking.factory;

import com.banking.model.Account;
import com.banking.model.CurrentAccount;
import com.banking.model.LoanAccount;
import com.banking.model.SavingsAccount;

public class AccountFactory {
    private static final double DEFAULT_SAVINGS_INTEREST=0.04;
    private static final double DEFUALT_LOAN_INTEREST=0.08;
    private static final double DEFAULT_OVERDRAFT_LIMIT=50000;
}
