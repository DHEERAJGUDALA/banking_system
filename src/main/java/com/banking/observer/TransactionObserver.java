package com.banking.observer;

import com.banking.model.Account;
import com.banking.model.Transaction;

public interface TransactionObserver {
    void onTransaction(Account account, Transaction transaction);
}
