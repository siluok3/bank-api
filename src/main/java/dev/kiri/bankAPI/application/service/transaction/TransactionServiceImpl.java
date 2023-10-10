package dev.kiri.bankAPI.application.service.transaction;

import dev.kiri.bankAPI.adapter.persistence.AccountRepository;
import dev.kiri.bankAPI.adapter.persistence.TransactionRepository;
import dev.kiri.bankAPI.application.exception.InsufficientFundsException;
import dev.kiri.bankAPI.application.exception.NotFoundException;
import dev.kiri.bankAPI.application.util.TransactionResponseFormatter;
import dev.kiri.bankAPI.domain.Account;
import dev.kiri.bankAPI.domain.Transaction;
import dev.kiri.bankAPI.domain.dto.transaction.TransactionRequestDTO;
import dev.kiri.bankAPI.domain.dto.transaction.TransactionResponseDTO;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionServiceImpl(
            TransactionRepository transactionRepository,
            AccountRepository accountRepository
    ) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    @Transactional
    public Transaction createTransaction(TransactionRequestDTO transactionRequest) {
        Optional<Account> fromAccountOptional = accountRepository.findById(transactionRequest.fromAccount());
        Optional<Account> toAccountOptional = accountRepository.findById(transactionRequest.toAccount());
        if (fromAccountOptional.isEmpty()) {
            throw new NotFoundException("The sender account does not exist for id: "+transactionRequest.fromAccount());
        }
        if (toAccountOptional.isEmpty()) {
            throw new NotFoundException("The recipient account does not exist for id: "+transactionRequest.toAccount());
        }

        Account senderAccount = fromAccountOptional.get();
        Account recepientAccount = toAccountOptional.get();

        double transactionAmount = transactionRequest.amount();

        if (senderAccount.getBalance() < transactionAmount) {
            throw new InsufficientFundsException("Insufficient funds in sender's account");
        }

        //Update Balance to both sender and recipient
        senderAccount.setBalance(senderAccount.getBalance() - transactionAmount);
        recepientAccount.setBalance(recepientAccount.getBalance() + transactionAmount);

        Transaction transaction = new Transaction();
        transaction.setFromAccount(senderAccount.getId());
        transaction.setToAccount(recepientAccount.getId());
        transaction.setAmount(transactionAmount);
        transaction.setTimestamp(LocalDateTime.now());

        accountRepository.save(senderAccount);
        accountRepository.save(recepientAccount);
        transactionRepository.save(transaction);

        return transaction;
    }

    @Override
    public TransactionResponseDTO getTransactionsByAccountId(Long accountId) {
        List<Transaction> accountTransactions = transactionRepository.findByFromAccountOrToAccount(accountId, accountId);

        return TransactionResponseFormatter.formatTransactions(accountTransactions, accountId);
    }
}
