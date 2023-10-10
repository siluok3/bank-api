package dev.kiri.bankAPI.adapter.controller;

import dev.kiri.bankAPI.application.service.transaction.TransactionService;
import dev.kiri.bankAPI.domain.Transaction;
import dev.kiri.bankAPI.domain.dto.transaction.TransactionRequestDTO;
import dev.kiri.bankAPI.domain.dto.transaction.TransactionResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@Valid @RequestBody TransactionRequestDTO transactionRequestDTO) {
        return ResponseEntity.ok(transactionService.createTransaction(transactionRequestDTO));
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<TransactionResponseDTO> getAccountTransactions(@PathVariable Long accountId) {
        return ResponseEntity.ok(transactionService.getTransactionsByAccountId(accountId));
    }
}
