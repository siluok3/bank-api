package dev.kiri.bankAPI.application.service.account;

import dev.kiri.bankAPI.adapter.persistence.AccountRepository;
import dev.kiri.bankAPI.application.exception.DuplicateException;
import dev.kiri.bankAPI.application.exception.InvalidRequestException;
import dev.kiri.bankAPI.application.exception.NotFoundException;
import dev.kiri.bankAPI.domain.Account;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class AccountServiceImplTest {
    @Mock private AccountRepository accountRepository;
    @InjectMocks private AccountServiceImpl accountService;

    @Test
    void testCreateAccount_Success() {
        Long userId = 1L;
        Account account = new Account(userId);
        when(accountRepository.findByUserId(userId)).thenReturn(Optional.empty());
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        Account createdAccount = accountService.createAccount(userId);

        assertEquals(account, createdAccount);
    }

    @Test
    void testCreateAccount_DuplicateException() {
        Long userId = 1L;
        Account account = new Account(userId);
        when(accountRepository.findByUserId(userId)).thenReturn(Optional.of(account));

        DuplicateException ex = assertThrows(DuplicateException.class, () -> accountService.createAccount(userId));
        assertEquals("Account already exists for user id: "+userId, ex.getMessage());
        verify(accountRepository, never()).save(account);
    }

    @Test
    void testGetAccountById_Success() {
        Long accountId = 1L;
        Account account = new Account(accountId);
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        Account fetchedAccount = accountService.getAccountById(accountId);

        assertEquals(account, fetchedAccount);
    }

    @Test
    void testGetAccount_NotFoundException() {
        Long accountId = 1L;
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class, () -> accountService.getAccountById(accountId));
        assertEquals("Account not found for id: "+accountId, ex.getMessage());
    }

    @Test
    void testDeposit_ValidAmount() {
        Long accountId = 1L;
        Account account = new Account(accountId);
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        accountService.deposit(accountId, 50.0);

        assertEquals(50.0, account.getBalance());
    }

    @Test
    void testDeposit_InvalidAmount() {
        Long accountId = 1L;
        Account account = new Account(accountId);
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        InvalidRequestException ex = assertThrows(InvalidRequestException.class,
                () -> accountService.deposit(accountId, -25.0));
        assertEquals("Amount should a positive number", ex.getMessage());
        verify(accountRepository, never()).save(account);
    }
}