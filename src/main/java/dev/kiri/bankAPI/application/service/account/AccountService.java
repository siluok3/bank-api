package dev.kiri.bankAPI.application.service.account;

import dev.kiri.bankAPI.domain.Account;

public interface AccountService {
    Account createAccount(Long userId);
    Account getAccountById(Long accountId);
    void deposit(Long accountId, double amount);
}
