package com.Legacy.LegacyBank.Service;

import com.Legacy.LegacyBank.Model.*;
import com.Legacy.LegacyBank.Repository.AccountRepository;
import com.Legacy.LegacyBank.Repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;
    
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @Override
    public Optional<Transaction> getTransactionById(Long id) {
        return transactionRepository.findById(id);
    }

    @Override
    public Transaction createTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public Transaction updateTransaction(Long id, Transaction transactionDetails) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found with id: " + id));
        
        // Update transaction fields
        if (transactionDetails.getDescription() != null) {
            transaction.setDescription(transactionDetails.getDescription());
        }
        if (transactionDetails.getAmount() != null) {
            transaction.setAmount(transactionDetails.getAmount());
        }
        if (transactionDetails.getType() != null) {
            transaction.setType(transactionDetails.getType());
        }
        if (transactionDetails.getStatus() != null) {
            transaction.setStatus(transactionDetails.getStatus());
        }
        
        return transactionRepository.save(transaction);
    }

    @Override
    public void deleteTransaction(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found with id: " + id));
        
        transactionRepository.delete(transaction);
    }
    
    @Override
    @Transactional
    public Transaction deposit(Long accountId, BigDecimal amount, String description) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + accountId));
        
        // Update account balance
        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);
        
        // Create transaction record
        Transaction transaction = new Transaction();
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setAmount(amount);
        transaction.setToAccount(account);
        transaction.setStatus(TransactionStatus.COMPLETED);
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setDescription(description != null ? description : "Deposit to account");
        
        return transactionRepository.save(transaction);
    }
    
    @Override
    @Transactional
    public Transaction withdraw(Long accountId, BigDecimal amount, String description) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + accountId));
        
        if (account.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }
        
        // Update account balance
        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);
        
        // Create transaction record
        Transaction transaction = new Transaction();
        transaction.setType(TransactionType.WITHDRAWAL);
        transaction.setAmount(amount);
        transaction.setFromAccount(account);
        transaction.setStatus(TransactionStatus.COMPLETED);
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setDescription(description != null ? description : "Withdrawal from account");
        
        return transactionRepository.save(transaction);
    }
    
    @Override
    @Transactional
    public Transaction transfer(Long fromAccountId, Long toAccountId, BigDecimal amount, String description) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transfer amount must be positive");
        }
        
        if (fromAccountId.equals(toAccountId)) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }
        
        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new RuntimeException("Source account not found with id: " + fromAccountId));
        
        Account toAccount = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new RuntimeException("Destination account not found with id: " + toAccountId));
        
        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }
        
        // Update account balances
        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));
        
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
        
        // Create transaction record
        Transaction transaction = new Transaction();
        transaction.setType(TransactionType.TRANSFER);
        transaction.setAmount(amount);
        transaction.setFromAccount(fromAccount);
        transaction.setToAccount(toAccount);
        transaction.setStatus(TransactionStatus.COMPLETED);
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setDescription(description != null ? description : "Transfer between accounts");
        
        return transactionRepository.save(transaction);
    }
    
    @Override
    public Page<Transaction> getTransactionHistory(Long accountId, String type, String status, Pageable pageable) {
        if (accountId != null) {
            Account account = accountRepository.findById(accountId)
                    .orElseThrow(() -> new RuntimeException("Account not found with id: " + accountId));
            
            // Find transactions where the account is either the source or destination
            return transactionRepository.findByFromAccountOrToAccount(account, account, pageable);
        }
        
        // If no account specified, return all transactions (with pagination)
        return transactionRepository.findAll(pageable);
    }
}