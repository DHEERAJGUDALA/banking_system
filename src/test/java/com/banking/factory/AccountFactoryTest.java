package com.banking.factory;

import com.banking.model.Account;
import com.banking.model.CurrentAccount;
import com.banking.model.LoanAccount;
import com.banking.model.SavingsAccount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AccountFactory Tests")
class AccountFactoryTest {

    @Test
    @DisplayName("should create SavingsAccount")
    void shouldCreateSavingsAccount() {
        Account account = AccountFactory.createAccount(
                AccountType.SAVINGS, "SA001", "Alice", 5000);

        assertInstanceOf(SavingsAccount.class, account);
        assertEquals("SA001", account.getAccountId());
        assertEquals("Alice", account.getHolderName());
        assertEquals(5000, account.getBalance());
        assertEquals("Savings", account.getAccountType());
    }

    @Test
    @DisplayName("should create CurrentAccount")
    void shouldCreateCurrentAccount() {
        Account account = AccountFactory.createAccount(
                AccountType.CURRENT, "CA001", "Bob", 10000);

        assertInstanceOf(CurrentAccount.class, account);
        assertEquals("CA001", account.getAccountId());
        assertEquals(10000, account.getBalance());
        assertEquals("Current", account.getAccountType());
    }

    @Test
    @DisplayName("should create LoanAccount")
    void shouldCreateLoanAccount() {
        Account account = AccountFactory.createAccount(
                AccountType.LOAN, "LA001", "Charlie", 500000);

        assertInstanceOf(LoanAccount.class, account);
        assertEquals("LA001", account.getAccountId());
        assertEquals(500000, account.getBalance());
        assertEquals("Loan", account.getAccountType());
    }

    @Test
    @DisplayName("should set default interest rate for savings")
    void shouldSetDefaultSavingsInterest() {
        Account account = AccountFactory.createAccount(
                AccountType.SAVINGS, "SA002", "Test", 1000);
        assertEquals(0.04, account.getInterestRate());
    }

    @Test
    @DisplayName("should set default interest rate for loan")
    void shouldSetDefaultLoanInterest() {
        Account account = AccountFactory.createAccount(
                AccountType.LOAN, "LA002", "Test", 100000);
        assertEquals(0.08, account.getInterestRate());
    }

    @Test
    @DisplayName("should set default overdraft for current")
    void shouldSetDefaultOverdraft() {
        Account account = AccountFactory.createAccount(
                AccountType.CURRENT, "CA002", "Test", 5000);
        assertInstanceOf(CurrentAccount.class, account);
        assertEquals(50000, ((CurrentAccount) account).getOverdraftLimit());
    }
}
