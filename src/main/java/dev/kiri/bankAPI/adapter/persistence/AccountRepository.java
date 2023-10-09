package dev.kiri.bankAPI.adapter.persistence;

import dev.kiri.bankAPI.domain.Account;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AccountRepository extends CrudRepository<Account, Long> {
    Optional<Account> findByUserId(Long userId);
}
