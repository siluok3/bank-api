package dev.kiri.bankAPI.application.service.transaction;

import dev.kiri.bankAPI.adapter.persistence.AccountRepository;
import dev.kiri.bankAPI.adapter.persistence.TransactionRepository;
import dev.kiri.bankAPI.application.exception.InsufficientFundsException;
import dev.kiri.bankAPI.application.exception.NotFoundException;
import dev.kiri.bankAPI.domain.Account;
import dev.kiri.bankAPI.domain.Transaction;
import dev.kiri.bankAPI.domain.dto.transaction.TransactionRequestDTO;
import dev.kiri.bankAPI.domain.dto.transaction.TransactionResponseDTO;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class TransactionServiceImplTest {
    @Mock private AccountRepository accountRepository;
    @Mock private TransactionRepository transactionRepository;
    @InjectMocks private TransactionServiceImpl transactionService;

    @Test
    void testCreateTransaction_Success() {
        Long fromAccount = 1L;
        Long toAccount = 2L;
        double amount = 10.0;
        TransactionRequestDTO requestDTO = new TransactionRequestDTO(
                fromAccount,
                toAccount,
                amount
        );
        Account senderAccount = new Account(fromAccount);
        senderAccount.setBalance(50.0);
        Account recipientAccount = new Account(toAccount);
        recipientAccount.setBalance(50.0);
        when(accountRepository.findById(fromAccount)).thenReturn(Optional.of(senderAccount));
        when(accountRepository.findById(toAccount)).thenReturn(Optional.of(recipientAccount));

        Transaction transaction = transactionService.createTransaction(requestDTO);

        assertEquals(senderAccount.getId(), transaction.getFromAccount());
        assertEquals(recipientAccount.getId(), transaction.getToAccount());
        assertEquals(amount, transaction.getAmount());
        assertNotNull(transaction.getTimestamp());
        assertEquals(40.0, senderAccount.getBalance());
        assertEquals(60.0, recipientAccount.getBalance());

        verify(accountRepository, times(1)).save(senderAccount);
        verify(accountRepository, times(1)).save(recipientAccount);
        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    void testCreateTransaction_SenderAccountNotFound() {
        TransactionRequestDTO requestDTO = new TransactionRequestDTO(1L, 2L, 10.0);
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> transactionService.createTransaction(requestDTO));
        assertEquals("The sender account does not exist for id: 1", ex.getMessage());

        verify(accountRepository, never()).save(any(Account.class));
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void testCreateTransaction_RecipientAccountNotFound() {
        TransactionRequestDTO requestDTO = new TransactionRequestDTO(1L, 2L, 10.0);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(new Account(1L)));
        when(accountRepository.findById(2L)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> transactionService.createTransaction(requestDTO));
        assertEquals("The recipient account does not exist for id: 2", ex.getMessage());

        verify(accountRepository, never()).save(any(Account.class));
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void testCreateTransaction_InsufficientFunds() {
        Long fromAccount = 1L;
        Long toAccount = 2L;
        double amount = 100.0;
        TransactionRequestDTO requestDTO = new TransactionRequestDTO(
                fromAccount,
                toAccount,
                amount
        );
        Account senderAccount = new Account(fromAccount);
        senderAccount.setBalance(50.0);
        Account recipientAccount = new Account(toAccount);
        recipientAccount.setBalance(50.0);
        when(accountRepository.findById(fromAccount)).thenReturn(Optional.of(senderAccount));
        when(accountRepository.findById(toAccount)).thenReturn(Optional.of(recipientAccount));

        InsufficientFundsException ex = assertThrows(InsufficientFundsException.class,
                () -> transactionService.createTransaction(requestDTO));
        assertEquals("Insufficient funds in sender's account", ex.getMessage());

        verify(accountRepository, never()).save(any(Account.class));
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void testGetTransactionsByAccountId() {
        Long accountId = 1L;
        Transaction transaction1 = new Transaction(1L, 2L, 10.0);
        Transaction transaction2 = new Transaction(3L, 1L, 10.0);
        when(transactionRepository.findByFromAccountOrToAccount(accountId, accountId))
                .thenReturn(List.of(transaction1, transaction2));

        TransactionResponseDTO responseDTO = transactionService.getTransactionsByAccountId(accountId);

        assertNotNull(responseDTO);
        assertEquals(1, responseDTO.sendTransactions().size());
        assertEquals(1, responseDTO.receivedTransactions().size());
    }
}