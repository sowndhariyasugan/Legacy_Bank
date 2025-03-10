package com.Legacy.LegacyBank.Service;

import com.Legacy.LegacyBank.Model.*;
import com.Legacy.LegacyBank.Repository.AccountRepository;
import com.Legacy.LegacyBank.Repository.TransactionRepository;
import com.Legacy.LegacyBank.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AccountService implements UserDetailsService {
    
    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    public Account findAccountById(String id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    public Account findAccountByNumber(String number) {
        return accountRepository.findByNumber(number)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    public List<Account> findAccountsByUserId(String userId) {
        return accountRepository.findByUserId(userId);
    }

    @Transactional
    public Account createAccount(String userId, AccountType type) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Account account = new Account();
        account.setNumber(generateAccountNumber());
        account.setType(type);
        account.setBalance(BigDecimal.ZERO);
        account.setUser(user);
        
        return accountRepository.save(account);
    }

    @Transactional
    public void deposit(String accountId, BigDecimal amount) {
        Account account = findAccountById(accountId);
        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);
        
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setStatus(TransactionStatus.COMPLETED);
        transaction.setAccount(account);
        transaction.setUser(account.getUser());
        transaction.setDescription("Deposit");
        transactionRepository.save(transaction);
    }
    
    @Transactional
    public void withdraw(String accountId, BigDecimal amount) {
        Account account = findAccountById(accountId);
        if (account.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient funds");
        }
        
        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);
        
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setType(TransactionType.WITHDRAW);
        transaction.setStatus(TransactionStatus.COMPLETED);
        transaction.setAccount(account);
        transaction.setUser(account.getUser());
        transaction.setDescription("Withdrawal");
        transactionRepository.save(transaction);
    }
    
    @Transactional
    public void transfer(String fromAccountId, String toAccountId, BigDecimal amount) {
        Account fromAccount = findAccountById(fromAccountId);
        Account toAccount = findAccountById(toAccountId);
        
        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient funds");
        }
        
        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));
        
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
        
        // Create debit transaction
        Transaction debitTransaction = new Transaction();
        debitTransaction.setAmount(amount);
        debitTransaction.setType(TransactionType.TRANSFER);
        debitTransaction.setStatus(TransactionStatus.COMPLETED);
        debitTransaction.setAccount(fromAccount);
        debitTransaction.setUser(fromAccount.getUser());
        debitTransaction.setDescription("Transfer to account " + toAccount.getNumber());
        
        // Create credit transaction
        Transaction creditTransaction = new Transaction();
        creditTransaction.setAmount(amount);
        creditTransaction.setType(TransactionType.TRANSFER);
        creditTransaction.setStatus(TransactionStatus.COMPLETED);
        creditTransaction.setAccount(toAccount);
        creditTransaction.setUser(toAccount.getUser());
        creditTransaction.setDescription("Transfer from account " + fromAccount.getNumber());
        
        transactionRepository.save(debitTransaction);
        transactionRepository.save(creditTransaction);
    }

    public List<Transaction> getTransactionHistory(String accountId) {
        return transactionRepository.findByAccountId(accountId);
    }

    private String generateAccountNumber() {
        // Generate a random 10-digit account number
        return String.format("%010d", (long)(Math.random() * 10000000000L));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return (UserDetails) userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
