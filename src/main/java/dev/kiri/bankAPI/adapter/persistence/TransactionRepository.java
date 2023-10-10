package dev.kiri.bankAPI.adapter.persistence;

import dev.kiri.bankAPI.domain.Transaction;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {
    List<Transaction> findByFromAccountOrToAccount(Long fromAccount, Long toAccount);
}
