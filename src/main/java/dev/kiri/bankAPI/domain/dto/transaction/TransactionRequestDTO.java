package dev.kiri.bankAPI.domain.dto.transaction;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record TransactionRequestDTO(
        @NotNull(message = "From account is mandatory") Long fromAccount,
        @NotNull(message = "To account is mandatory")  Long toAccount,
        @Positive(message = "Amount must be positive") double amount) {
}
