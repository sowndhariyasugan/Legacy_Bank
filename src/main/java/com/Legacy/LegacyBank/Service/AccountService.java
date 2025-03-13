package com.Legacy.LegacyBank.Service;

import com.Legacy.LegacyBank.Model.Account;
import com.Legacy.LegacyBank.Model.AccountType;
import com.Legacy.LegacyBank.Model.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface AccountService {
    List<Account> getAllAccounts();
    
    List<Account> getAccountsByUserId(Long userId);
    
    Optional<Account> getAccountById(Long id);
    
    Optional<Account> getAccountByNumber(String accountNumber);
    
    Account createAccount(User user, AccountType accountType, String currency);
    
    Account updateAccount(Long id, Account accountDetails);
    
    void deleteAccount(Long id);
    
    Account deposit(Long accountId, BigDecimal amount);
    
    Account withdraw(Long accountId, BigDecimal amount);
    
    void transfer(Long fromAccountId, Long toAccountId, BigDecimal amount);
    
    BigDecimal getBalance(Long accountId);
}