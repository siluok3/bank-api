package dev.kiri.bankAPI.application.util;

import dev.kiri.bankAPI.domain.Transaction;
import dev.kiri.bankAPI.domain.dto.transaction.TransactionDTO;
import dev.kiri.bankAPI.domain.dto.transaction.TransactionResponseDTO;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TransactionResponseFormatterTest {
    @Test
    void testFormatTransactions() {
        Long accountId = 1L;
        List<Transaction> transactions = new ArrayList<>();
        Transaction transaction = new Transaction(1L, 2L, 10.0);
        transactions.add(transaction);

        TransactionResponseDTO response = TransactionResponseFormatter.formatTransactions(transactions, accountId);

        assertEquals(1, response.sendTransactions().size());
        assertEquals(0, response.receivedTransactions().size());
        assertEquals(-10.0, response.sendTransactions().get(0).getAmount());
    }

    @Test
    void testMapTransaction() {
        Long accountId = 1L;
        Transaction transaction = new Transaction(1L, 2L, 10.0);

        TransactionDTO transactionDTO = TransactionResponseFormatter.mapTransaction(transaction, accountId);

        assertEquals(2L, transactionDTO.getAccount());
        assertEquals(-10.0, transactionDTO.getAmount());
    }
}