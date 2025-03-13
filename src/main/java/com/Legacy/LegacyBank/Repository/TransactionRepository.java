package com.Legacy.LegacyBank.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Legacy.LegacyBank.Model.Account;
import com.Legacy.LegacyBank.Model.Transaction;
import com.Legacy.LegacyBank.Model.TransactionStatus;
import com.Legacy.LegacyBank.Model.TransactionType;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    // Find by account (either source or destination)
    Page<Transaction> findByFromAccountOrToAccount(Account fromAccount, Account toAccount, Pageable pageable);
    
    // Find by fromAccount
    Page<Transaction> findByFromAccount(Account fromAccount, Pageable pageable);
    
    // Find by toAccount
    Page<Transaction> findByToAccount(Account toAccount, Pageable pageable);
    
    // Find by type
    Page<Transaction> findByType(TransactionType type, Pageable pageable);
    
    // Find by status
    Page<Transaction> findByStatus(TransactionStatus status, Pageable pageable);
    
    // Find by timestamp range
    Page<Transaction> findByTimestampBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    
    // Combined queries
    Page<Transaction> findByFromAccountAndType(Account fromAccount, TransactionType type, Pageable pageable);
    Page<Transaction> findByToAccountAndType(Account toAccount, TransactionType type, Pageable pageable);
    Page<Transaction> findByFromAccountAndStatus(Account fromAccount, TransactionStatus status, Pageable pageable);
    Page<Transaction> findByToAccountAndStatus(Account toAccount, TransactionStatus status, Pageable pageable);
}