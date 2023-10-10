package dev.kiri.bankAPI.adapter.controller;

import dev.kiri.bankAPI.application.service.account.AccountService;
import dev.kiri.bankAPI.domain.Account;
import dev.kiri.bankAPI.domain.dto.account.AccountDepositDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/{userId}")
    public ResponseEntity<Account> createAccount(@PathVariable Long userId) {
        return ResponseEntity.ok(accountService.createAccount(userId));
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<Account> getAccount(@PathVariable Long accountId) {
        return ResponseEntity.ok(accountService.getAccountById(accountId));
    }

    @PostMapping("/{accountId}/deposit")
    @ResponseStatus(HttpStatus.OK)
    public void deposit(@PathVariable Long accountId, @RequestBody AccountDepositDTO accountDepositDTO) {
        accountService.deposit(accountId, accountDepositDTO.amount());
    }
}
