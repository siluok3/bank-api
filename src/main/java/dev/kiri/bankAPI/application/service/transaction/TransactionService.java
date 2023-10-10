package dev.kiri.bankAPI.application.service.transaction;

import dev.kiri.bankAPI.domain.Transaction;
import dev.kiri.bankAPI.domain.dto.transaction.TransactionRequestDTO;
import dev.kiri.bankAPI.domain.dto.transaction.TransactionResponseDTO;

public interface TransactionService {
    Transaction createTransaction(TransactionRequestDTO transaction);
    TransactionResponseDTO getTransactionsByAccountId(Long accountId);
}
