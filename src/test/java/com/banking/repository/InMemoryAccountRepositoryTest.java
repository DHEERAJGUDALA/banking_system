package com.banking.repository;

import com.banking.model.Account;
import com.banking.model.SavingsAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("InMemoryAccountRepository Tests")
class InMemoryAccountRepositoryTest {
    private InMemoryAccountRepository repository;
    private Account testAccount;

    @BeforeEach
    void setUp() {
        repository = new InMemoryAccountRepository();
        testAccount = new SavingsAccount.Builder("SA001", "Alice")
                .balance(10000)
                .build();
    }

    @Test
    @DisplayName("should save and find account by ID")
    void shouldSaveAndFind() {
        repository.save(testAccount);
        Optional<Account> found = repository.findById("SA001");

        assertTrue(found.isPresent());
        assertEquals("Alice", found.get().getHolderName());
    }

    @Test
    @DisplayName("should return empty Optional for missing account")
    void shouldReturnEmptyForMissing() {
        Optional<Account> found = repository.findById("INVALID");
        assertTrue(found.isEmpty());
    }

    @Test
    @DisplayName("should find all accounts")
    void shouldFindAll() {
        repository.save(testAccount);
        Account second = new SavingsAccount.Builder("SA002", "Bob")
                .balance(5000).build();
        repository.save(second);

        List<Account> all = repository.findAll();
        assertEquals(2, all.size());
    }

    @Test
    @DisplayName("should find accounts by holder name (case insensitive)")
    void shouldFindByHolderName() {
        repository.save(testAccount);
        List<Account> found = repository.findByHolderName("alice");
        assertEquals(1, found.size());
        assertEquals("SA001", found.get(0).getAccountId());
    }

    @Test
    @DisplayName("should delete account")
    void shouldDelete() {
        repository.save(testAccount);
        assertTrue(repository.exists("SA001"));

        repository.delete("SA001");
        assertFalse(repository.exists("SA001"));
    }

    @Test
    @DisplayName("should check existence correctly")
    void shouldCheckExistence() {
        assertFalse(repository.exists("SA001"));
        repository.save(testAccount);
        assertTrue(repository.exists("SA001"));
    }

    @Test
    @DisplayName("should overwrite on duplicate save")
    void shouldOverwriteOnDuplicateSave() {
        repository.save(testAccount);
        Account updated = new SavingsAccount.Builder("SA001", "Alice Updated")
                .balance(99999).build();
        repository.save(updated);

        assertEquals(1, repository.findAll().size());
        assertEquals("Alice Updated", repository.findById("SA001").get().getHolderName());
    }
}
