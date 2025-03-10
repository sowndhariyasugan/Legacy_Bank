package com.Legacy.LegacyBank.Repository;

import com.Legacy.LegacyBank.Model.Transaction;
import com.Legacy.LegacyBank.Model.TransactionStatus;
import com.Legacy.LegacyBank.Model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, String> {
    List<Transaction> findByAccountId(String accountId);
    List<Transaction> findByUserId(String userId);
    List<Transaction> findByCardId(String cardId);
    List<Transaction> findByStatus(TransactionStatus status);
    List<Transaction> findByType(TransactionType type);
    List<Transaction> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
