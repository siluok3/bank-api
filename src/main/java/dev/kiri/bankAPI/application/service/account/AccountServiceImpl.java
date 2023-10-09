package dev.kiri.bankAPI.application.service.account;

import dev.kiri.bankAPI.adapter.persistence.AccountRepository;
import dev.kiri.bankAPI.application.exception.DuplicateException;
import dev.kiri.bankAPI.application.exception.InvalidRequestException;
import dev.kiri.bankAPI.application.exception.NotFoundException;
import dev.kiri.bankAPI.domain.Account;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account createAccount(Long userId) {
        if (accountRepository.findByUserId(userId).isPresent()) {
            throw new DuplicateException("Account already exists for user id: "+userId);
        }
        Account account = new Account();
        account.setUserId(userId);

        return accountRepository.save(account);
    }

    @Override
    public Account getAccountById(Long accountId) {
        return accountRepository
                .findById(accountId)
                .orElseThrow(() -> new NotFoundException("Account not found for id: "+accountId));
    }

    @Override
    public void deposit(Long accountId, double amount) {
        if (amount <= 0.0) {
            throw new InvalidRequestException("Amount should a positive number");
        }
        Account account = getAccountById(accountId);
        double currentBalance = account.getBalance();
        account.setBalance(currentBalance + amount);

        accountRepository.save(account);
    }
}
