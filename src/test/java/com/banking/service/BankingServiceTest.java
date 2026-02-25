package com.banking.service;

import com.banking.exception.AccountNotFoundException;
import com.banking.exception.InsufficientFundsException;
import com.banking.exception.InvalidTransactionException;
import com.banking.factory.AccountType;
import com.banking.model.Account;
import com.banking.model.Transaction;
import com.banking.observer.TransactionObserver;
import com.banking.repository.InMemoryAccountRepository;
import com.banking.strategy.SimpleInterestStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("BankingService Tests")
class BankingServiceTest {
    private BankingService service;
    private InMemoryAccountRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryAccountRepository();
        service = new BankingService(repository);
    }

    @Nested
    @DisplayName("Account Creation")
    class AccountCreation {

        @Test
        @DisplayName("should create savings account")
        void shouldCreateSavings() {
            Account account = service.createAccount(
                    AccountType.SAVINGS, "SA001", "Alice", 5000);
            assertEquals("SA001", account.getAccountId());
            assertEquals(5000, account.getBalance());
        }

        @Test
        @DisplayName("should throw on duplicate account ID")
        void shouldThrowOnDuplicate() {
            service.createAccount(AccountType.SAVINGS, "SA001", "Alice", 5000);
            assertThrows(InvalidTransactionException.class,
                    () -> service.createAccount(AccountType.SAVINGS, "SA001", "Bob", 3000));
        }
    }

    @Nested
    @DisplayName("Deposits")
    class Deposits {

        @Test
        @DisplayName("should deposit successfully")
        void shouldDeposit() {
            service.createAccount(AccountType.SAVINGS, "SA001", "Alice", 5000);
            service.deposit("SA001", 3000);
            assertEquals(8000, service.getAccount("SA001").getBalance());
        }

        @Test
        @DisplayName("should throw on deposit to non-existent account")
        void shouldThrowOnMissingAccount() {
            assertThrows(AccountNotFoundException.class,
                    () -> service.deposit("INVALID", 1000));
        }
    }

    @Nested
    @DisplayName("Withdrawals")
    class Withdrawals {

        @Test
        @DisplayName("should withdraw successfully")
        void shouldWithdraw() {
            service.createAccount(AccountType.SAVINGS, "SA001", "Alice", 5000);
            service.withdraw("SA001", 2000);
            assertEquals(3000, service.getAccount("SA001").getBalance());
        }

        @Test
        @DisplayName("should throw on insufficient funds")
        void shouldThrowOnInsufficientFunds() {
            service.createAccount(AccountType.SAVINGS, "SA001", "Alice", 5000);
            assertThrows(InsufficientFundsException.class,
                    () -> service.withdraw("SA001", 50000));
        }
    }

    @Nested
    @DisplayName("Transfers")
    class Transfers {

        @Test
        @DisplayName("should transfer between accounts")
        void shouldTransfer() {
            service.createAccount(AccountType.SAVINGS, "SA001", "Alice", 10000);
            service.createAccount(AccountType.CURRENT, "CA001", "Bob", 5000);

            service.transfer("SA001", "CA001", 3000);

            assertEquals(7000, service.getAccount("SA001").getBalance());
            assertEquals(8000, service.getAccount("CA001").getBalance());
        }

        @Test
        @DisplayName("should throw on transfer to same account")
        void shouldThrowOnSameAccount() {
            service.createAccount(AccountType.SAVINGS, "SA001", "Alice", 10000);
            assertThrows(InvalidTransactionException.class,
                    () -> service.transfer("SA001", "SA001", 1000));
        }

        @Test
        @DisplayName("should throw on transfer with insufficient funds")
        void shouldThrowOnInsufficientTransfer() {
            service.createAccount(AccountType.SAVINGS, "SA001", "Alice", 1000);
            service.createAccount(AccountType.CURRENT, "CA001", "Bob", 5000);
            assertThrows(InsufficientFundsException.class,
                    () -> service.transfer("SA001", "CA001", 50000));
        }
    }

    @Nested
    @DisplayName("Interest")
    class Interest {

        @Test
        @DisplayName("should calculate interest using account method")
        void shouldCalculateInterestDefault() {
            service.createAccount(AccountType.SAVINGS, "SA001", "Alice", 10000);
            double interest = service.calculateInterest("SA001");
            assertTrue(interest > 0);
        }

        @Test
        @DisplayName("should calculate interest using strategy")
        void shouldCalculateInterestWithStrategy() {
            service.createAccount(AccountType.SAVINGS, "SA001", "Alice", 10000);
            service.setInterestStrategy(new SimpleInterestStrategy());
            double interest = service.calculateInterest("SA001");
            assertEquals(400, interest, 0.01);
        }
    }

    @Nested
    @DisplayName("Observers")
    class Observers {

        @Test
        @DisplayName("should notify observers on deposit")
        void shouldNotifyOnDeposit() {
            service.createAccount(AccountType.SAVINGS, "SA001", "Alice", 5000);

            final boolean[] notified = {false};
            service.addObserver((account, txn) -> notified[0] = true);

            service.deposit("SA001", 1000);
            assertTrue(notified[0]);
        }

        @Test
        @DisplayName("should notify observers on withdrawal")
        void shouldNotifyOnWithdraw() {
            service.createAccount(AccountType.SAVINGS, "SA001", "Alice", 5000);

            final boolean[] notified = {false};
            service.addObserver((account, txn) -> notified[0] = true);

            service.withdraw("SA001", 1000);
            assertTrue(notified[0]);
        }

        @Test
        @DisplayName("should stop notifying after observer removed")
        void shouldStopNotifyingAfterRemoval() {
            service.createAccount(AccountType.SAVINGS, "SA001", "Alice", 5000);

            final int[] count = {0};
            TransactionObserver observer = (account, txn) -> count[0]++;

            service.addObserver(observer);
            service.deposit("SA001", 1000);
            assertEquals(1, count[0]);

            service.removeObserver(observer);
            service.deposit("SA001", 1000);
            assertEquals(1, count[0]);
        }
    }

    @Nested
    @DisplayName("Transaction History")
    class TransactionHistory {

        @Test
        @DisplayName("should return transaction history")
        void shouldReturnHistory() {
            service.createAccount(AccountType.SAVINGS, "SA001", "Alice", 5000);
            service.deposit("SA001", 1000);
            service.withdraw("SA001", 500);

            List<Transaction> history = service.getTransactionHistory("SA001");
            assertEquals(2, history.size());
        }

        @Test
        @DisplayName("should throw for non-existent account history")
        void shouldThrowForMissingAccountHistory() {
            assertThrows(AccountNotFoundException.class,
                    () -> service.getTransactionHistory("INVALID"));
        }
    }

    @Nested
    @DisplayName("Get All Accounts")
    class GetAllAccounts {

        @Test
        @DisplayName("should return all accounts")
        void shouldReturnAll() {
            service.createAccount(AccountType.SAVINGS, "SA001", "Alice", 5000);
            service.createAccount(AccountType.CURRENT, "CA001", "Bob", 10000);
            assertEquals(2, service.getAllAccounts().size());
        }
    }
}
