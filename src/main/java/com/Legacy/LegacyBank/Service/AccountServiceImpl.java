package com.Legacy.LegacyBank.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Legacy.LegacyBank.Model.Account;
import com.Legacy.LegacyBank.Model.AccountType;
import com.Legacy.LegacyBank.Model.User;
import com.Legacy.LegacyBank.Repository.AccountRepository;
import com.Legacy.LegacyBank.Repository.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

// public interface AccountService {
//     List<Account> getAllAccounts();
    
//     List<Account> getAccountsByUserId(Long userId);
    
//     Optional<Account> getAccountById(Long id);
    
//     Optional<Account> getAccountByNumber(String accountNumber);
    
//     Account createAccount(User user, AccountType accountType, String currency);
    
//     Account updateAccount(Long id, Account accountDetails);
    
//     void deleteAccount(Long id);
    
//     Account deposit(Long accountId, BigDecimal amount);
    
//     Account withdraw(Long accountId, BigDecimal amount);
    
//     void transfer(Long fromAccountId, Long toAccountId, BigDecimal amount);
    
//     BigDecimal getBalance(Long accountId);
// }

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Override
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }
    
    @Override
    public List<Account> getAccountsByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        return accountRepository.findByUser(user);
    }

    @Override
    public Optional<Account> getAccountById(Long id) {
        return accountRepository.findById(id);
    }
    
    @Override
    public Optional<Account> getAccountByNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
    }
    private String getAuthenticatedUsername() {
        // Get the authentication object from the security context
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return null; // Handle non-authenticated state
        }
    }
    @Override
    @Transactional
    public Account createAccount(User user, AccountType accountType, String currency) {
        Account account = new Account();
        account.setAccountType(accountType);
        account.setBalance(BigDecimal.ZERO);
        account.setCurrency(currency);
        account.setStatus("ACTIVE");
        account.setCreatedAt(LocalDateTime.now());
        account.setAccountNumber(generateAccountNumber());
        account.setUser(user);
        
        return accountRepository.save(account);
    }
    
    @Override
    public Account updateAccount(Long id, Account accountDetails) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + id));
        
        // Update account fields
        if (accountDetails.getAccountType() != null) {
            account.setAccountType(accountDetails.getAccountType());
        }
        
        if (accountDetails.getCurrency() != null) {
            account.setCurrency(accountDetails.getCurrency());
        }
        
        if (accountDetails.getStatus() != null) {
            account.setStatus(accountDetails.getStatus());
        }
        
        // Don't allow direct balance updates through this method
        
        return accountRepository.save(account);
    }
    
    @Override
    public void deleteAccount(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + id));
        
        // Check if account has zero balance before deletion
        if (account.getBalance().compareTo(BigDecimal.ZERO) != 0) {
            throw new RuntimeException("Cannot delete account with non-zero balance");
        }
        
        accountRepository.delete(account);
    }
    
    private String generateAccountNumber() {
        // Generate a random account number
        return "ACC" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    @Override
    @Transactional
    public Account deposit(Long accountId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + accountId));
        
        account.setBalance(account.getBalance().add(amount));
        return accountRepository.save(account);
    }
    
    @Override
    @Transactional
    public Account withdraw(Long accountId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + accountId));
        
        if (account.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient funds");
        }
        
        account.setBalance(account.getBalance().subtract(amount));
        return accountRepository.save(account);
    }
    
    @Override
    @Transactional
    public void transfer(Long fromAccountId, Long toAccountId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transfer amount must be positive");
        }
        
        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new RuntimeException("Source account not found with id: " + fromAccountId));
        
        Account toAccount = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new RuntimeException("Target account not found with id: " + toAccountId));
        
        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient funds in source account");
        }
        
        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));
        
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
    }
    
    @Override
    public BigDecimal getBalance(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + accountId));
        
        return account.getBalance();
    }
}