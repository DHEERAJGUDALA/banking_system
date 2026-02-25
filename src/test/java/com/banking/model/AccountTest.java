package com.banking.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Account Tests")
class AccountTest {

    // --- SavingsAccount Tests ---

    @Nested
    @DisplayName("SavingsAccount")
    class SavingsAccountTests {
        private SavingsAccount account;

        @BeforeEach
        void setUp() {
            account = new SavingsAccount.Builder("SA001", "Alice")
                    .balance(10000)
                    .interestRate(0.04)
                    .minBalanceForInterest(1000)
                    .build();
        }

        @Test
        @DisplayName("should create account with correct values")
        void shouldCreateWithCorrectValues() {
            assertEquals("SA001", account.getAccountId());
            assertEquals("Alice", account.getHolderName());
            assertEquals(10000, account.getBalance());
            assertEquals(0.04, account.getInterestRate());
            assertEquals("Savings", account.getAccountType());
            assertTrue(account.isActive());
        }

        @Test
        @DisplayName("should deposit money successfully")
        void shouldDeposit() {
            account.deposit(5000);
            assertEquals(15000, account.getBalance());
            assertEquals(1, account.getTransactions().size());
        }

        @Test
        @DisplayName("should throw on negative deposit")
        void shouldThrowOnNegativeDeposit() {
            assertThrows(IllegalArgumentException.class, () -> account.deposit(-100));
        }

        @Test
        @DisplayName("should throw on zero deposit")
        void shouldThrowOnZeroDeposit() {
            assertThrows(IllegalArgumentException.class, () -> account.deposit(0));
        }

        @Test
        @DisplayName("should withdraw money successfully")
        void shouldWithdraw() {
            account.withdraw(3000);
            assertEquals(7000, account.getBalance());
        }

        @Test
        @DisplayName("should throw on insufficient funds")
        void shouldThrowOnInsufficientFunds() {
            assertThrows(IllegalArgumentException.class, () -> account.withdraw(20000));
        }

        @Test
        @DisplayName("should throw on negative withdrawal")
        void shouldThrowOnNegativeWithdrawal() {
            assertThrows(IllegalArgumentException.class, () -> account.withdraw(-100));
        }

        @Test
        @DisplayName("should calculate interest when balance meets minimum")
        void shouldCalculateInterest() {
            double interest = account.calculateInterest();
            assertEquals(400, interest, 0.01);
            assertEquals(10400, account.getBalance(), 0.01);
        }

        @Test
        @DisplayName("should return 0 interest when balance below minimum")
        void shouldReturnZeroInterestBelowMinimum() {
            SavingsAccount lowBalance = new SavingsAccount.Builder("SA002", "Bob")
                    .balance(500)
                    .interestRate(0.04)
                    .minBalanceForInterest(1000)
                    .build();
            assertEquals(0, lowBalance.calculateInterest());
        }

        @Test
        @DisplayName("should record transactions")
        void shouldRecordTransactions() {
            account.deposit(1000);
            account.withdraw(500);
            assertEquals(2, account.getTransactions().size());
        }

        @Test
        @DisplayName("should return unmodifiable transaction list")
        void shouldReturnUnmodifiableList() {
            account.deposit(1000);
            assertThrows(UnsupportedOperationException.class,
                    () -> account.getTransactions().add(null));
        }
    }

    // --- CurrentAccount Tests ---

    @Nested
    @DisplayName("CurrentAccount")
    class CurrentAccountTests {
        private CurrentAccount account;

        @BeforeEach
        void setUp() {
            account = new CurrentAccount.Builder("CA001", "Bob")
                    .balance(10000)
                    .overdraftLimit(20000)
                    .build();
        }

        @Test
        @DisplayName("should create with correct values")
        void shouldCreateWithCorrectValues() {
            assertEquals("CA001", account.getAccountId());
            assertEquals(10000, account.getBalance());
            assertEquals(20000, account.getOverdraftLimit());
            assertEquals("Current", account.getAccountType());
        }

        @Test
        @DisplayName("should withdraw within balance")
        void shouldWithdrawWithinBalance() {
            account.withdraw(5000);
            assertEquals(5000, account.getBalance());
        }

        @Test
        @DisplayName("should allow overdraft withdrawal")
        void shouldAllowOverdraft() {
            account.withdraw(25000);
            assertEquals(-15000, account.getBalance());
        }

        @Test
        @DisplayName("should throw when exceeding overdraft limit")
        void shouldThrowOnExceedingOverdraft() {
            assertThrows(IllegalArgumentException.class, () -> account.withdraw(35000));
        }

        @Test
        @DisplayName("should return 0 interest")
        void shouldReturnZeroInterest() {
            assertEquals(0, account.calculateInterest());
        }
    }

    // --- LoanAccount Tests ---

    @Nested
    @DisplayName("LoanAccount")
    class LoanAccountTests {
        private LoanAccount account;

        @BeforeEach
        void setUp() {
            account = new LoanAccount.Builder("LA001", "Charlie", 500000)
                    .interestRate(0.01)
                    .build();
        }

        @Test
        @DisplayName("should create with loan amount as balance")
        void shouldCreateWithLoanAmount() {
            assertEquals(500000, account.getBalance());
            assertEquals(500000, account.getOriginalLoanAmount());
            assertEquals("Loan", account.getAccountType());
        }

        @Test
        @DisplayName("should reduce balance on deposit (payment)")
        void shouldReduceBalanceOnDeposit() {
            account.deposit(50000);
            assertEquals(450000, account.getBalance());
        }

        @Test
        @DisplayName("should throw on overpayment")
        void shouldThrowOnOverpayment() {
            assertThrows(IllegalArgumentException.class, () -> account.deposit(600000));
        }

        @Test
        @DisplayName("should throw on withdraw")
        void shouldThrowOnWithdraw() {
            assertThrows(UnsupportedOperationException.class, () -> account.withdraw(1000));
        }

        @Test
        @DisplayName("should calculate interest and increase balance")
        void shouldCalculateInterest() {
            double interest = account.calculateInterest();
            assertEquals(5000, interest, 0.01);
            assertEquals(505000, account.getBalance(), 0.01);
        }

        @Test
        @DisplayName("should return 0 interest when fully paid")
        void shouldReturnZeroWhenPaid() {
            account.deposit(500000);
            assertEquals(0, account.calculateInterest());
        }

        @Test
        @DisplayName("getRemainingBalance should match getBalance")
        void remainingBalanceShouldMatchBalance() {
            account.deposit(100000);
            assertEquals(account.getBalance(), account.getRemainingBalance());
        }
    }
}
