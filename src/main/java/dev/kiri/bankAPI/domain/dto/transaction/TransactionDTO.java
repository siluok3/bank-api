package dev.kiri.bankAPI.domain.dto.transaction;

import java.time.LocalDateTime;

public class TransactionDTO {
    private Long account;
    private double amount;
    private LocalDateTime timestamp;

    public TransactionDTO() {

    }

    public Long getAccount() {
        return account;
    }

    public void setAccount(Long account) {
        this.account = account;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
