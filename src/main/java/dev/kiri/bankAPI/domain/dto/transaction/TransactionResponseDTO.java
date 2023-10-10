package dev.kiri.bankAPI.domain.dto.transaction;

import java.util.List;

public record TransactionResponseDTO(
        List<TransactionDTO> sendTransactions,
        List<TransactionDTO> receivedTransactions
) {
}
