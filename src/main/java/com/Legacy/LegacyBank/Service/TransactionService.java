package com.Legacy.LegacyBank.Service;

import com.Legacy.LegacyBank.Model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface TransactionService {
    List<Transaction> getAllTransactions();
    
    Optional<Transaction> getTransactionById(Long id);
    
    Transaction createTransaction(Transaction transaction);
    
    Transaction updateTransaction(Long id, Transaction transactionDetails);
    
    void deleteTransaction(Long id);
    
    Transaction deposit(Long accountId, BigDecimal amount, String description);
    
    Transaction withdraw(Long accountId, BigDecimal amount, String description);
    
    Transaction transfer(Long fromAccountId, Long toAccountId, BigDecimal amount, String description);
    
    Page<Transaction> getTransactionHistory(Long accountId, String type, String status, Pageable pageable);
}