package dev.kiri.bankAPI.application.util;

import dev.kiri.bankAPI.domain.Transaction;
import dev.kiri.bankAPI.domain.dto.transaction.TransactionDTO;
import dev.kiri.bankAPI.domain.dto.transaction.TransactionResponseDTO;

import java.util.List;

public class TransactionResponseFormatter {
    public static TransactionResponseDTO formatTransactions(List<Transaction> transactions, Long accountId) {
        List<TransactionDTO> sendTransactions = transactions.stream()
                .filter(transaction -> transaction.getFromAccount().equals(accountId))
                .map(transaction -> mapTransaction(transaction, accountId))
                .toList();

        List<TransactionDTO> receivedTransactions = transactions.stream()
                .filter(transaction -> transaction.getToAccount().equals(accountId))
                .map(transaction -> mapTransaction(transaction, accountId))
                .toList();

        return new TransactionResponseDTO(sendTransactions,receivedTransactions);
    }

    public static TransactionDTO mapTransaction(Transaction transaction, Long accountId) {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setAccount(transaction.getFromAccount().equals(accountId) ? transaction.getToAccount() : transaction.getFromAccount());
        transactionDTO.setAmount(transaction.getFromAccount().equals(accountId) ? -transaction.getAmount() : transaction.getAmount());
        transactionDTO.setTimestamp(transaction.getTimestamp());

        return transactionDTO;
    }
}
