package com.Legacy.LegacyBank.Controller;

import com.Legacy.LegacyBank.Model.Account;
import com.Legacy.LegacyBank.Model.AccountType;
import com.Legacy.LegacyBank.Model.User;
import com.Legacy.LegacyBank.Service.AccountService;
import com.Legacy.LegacyBank.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;
    
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts() {
        // Get current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        
        // Return only the accounts belonging to the authenticated user
        List<Account> accounts = accountService.getAccountsByUserId(user.getId());
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAccountById(@PathVariable Long id) {
        // Get current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        
        // Check if the account belongs to the authenticated user
        return accountService.getAccountById(id)
                .map(account -> {
                    if (!account.getUser().getId().equals(user.getId())) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                .body(Map.of("error", "You do not have permission to access this account"));
                    }
                    return ResponseEntity.ok(account);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createAccount(@Valid @RequestBody Map<String, String> request) {
        try {
            // Get current authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            User user = userService.findByUsername(username);
            
            // Validate input
            String accountTypeStr = request.get("accountType");
            if (accountTypeStr == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Account type is required"));
            }
            
            AccountType accountType;
            try {
                accountType = AccountType.valueOf(accountTypeStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(Map.of("error", "Invalid account type"));
            }
            
            String currency = request.get("currency");
            if (currency == null) {
                currency = "USD"; // Default currency
            }
            
            // Create account
            Account account = accountService.createAccount(user, accountType, currency);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(account);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Account> updateAccount(@PathVariable Long id, @RequestBody Account accountDetails) {
        try {
            Account updatedAccount = accountService.updateAccount(id, accountDetails);
            return ResponseEntity.ok(updatedAccount);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        try {
            accountService.deleteAccount(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user/{userId}")
    public List<Account> getAccountsByUserId(@PathVariable Long userId) {
        return accountService.getAccountsByUserId(userId);
    }

    @GetMapping("/number/{accountNumber}")
    public ResponseEntity<Account> getAccountByNumber(@PathVariable String accountNumber) {
        return accountService.getAccountByNumber(accountNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/deposit")
    public ResponseEntity<Account> deposit(@PathVariable Long id, @RequestBody Map<String, BigDecimal> request) {
        BigDecimal amount = request.get("amount");
        if (amount == null) {
            return ResponseEntity.badRequest().build();
        }
        
        try {
            Account account = accountService.deposit(id, amount);
            return ResponseEntity.ok(account);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    
    @PostMapping("/{id}/withdraw")
    public ResponseEntity<Account> withdraw(@PathVariable Long id, @RequestBody Map<String, BigDecimal> request) {
        BigDecimal amount = request.get("amount");
        if (amount == null) {
            return ResponseEntity.badRequest().build();
        }
        
        try {
            Account account = accountService.withdraw(id, amount);
            return ResponseEntity.ok(account);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    
    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestBody Map<String, Object> request) {
        try {
            Long fromAccountId = Long.valueOf(request.get("fromAccountId").toString());
            Long toAccountId = Long.valueOf(request.get("toAccountId").toString());
            BigDecimal amount = new BigDecimal(request.get("amount").toString());
            
            accountService.transfer(fromAccountId, toAccountId, amount);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @GetMapping("/{id}/balance")
    public ResponseEntity<Map<String, BigDecimal>> getBalance(@PathVariable Long id) {
        try {
            BigDecimal balance = accountService.getBalance(id);
            return ResponseEntity.ok(Map.of("balance", balance));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}