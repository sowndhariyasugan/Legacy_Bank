package com.Legacy.LegacyBank.Controller;

import com.Legacy.LegacyBank.Model.Account;
import com.Legacy.LegacyBank.Model.AccountType;
import com.Legacy.LegacyBank.Model.Transaction;
import com.Legacy.LegacyBank.Service.AccountService;
import com.Legacy.LegacyBank.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class BankController {

    @Autowired
    private AccountService accountService;
    
    @Autowired
    private UserService userService;

    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> getUserAccounts() {
        String userId = getCurrentUserId();
        List<Account> accounts = accountService.findAccountsByUserId(userId);
        return ResponseEntity.ok(accounts);
    }

    @PostMapping("/accounts")
    public ResponseEntity<Account> createAccount(@RequestBody Map<String, AccountType> request) {
        String userId = getCurrentUserId();
        Account account = accountService.createAccount(userId, request.get("type"));
        return new ResponseEntity<>(account, HttpStatus.CREATED);
    }

    @PostMapping("/accounts/{accountId}/deposit")
    public ResponseEntity<?> deposit(
            @PathVariable String accountId,
            @RequestBody Map<String, BigDecimal> request) {
        try {
            accountService.deposit(accountId, request.get("amount"));
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/accounts/{accountId}/withdraw")
    public ResponseEntity<?> withdraw(
            @PathVariable String accountId,
            @RequestBody Map<String, BigDecimal> request) {
        try {
            accountService.withdraw(accountId, request.get("amount"));
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/accounts/transfer")
    public ResponseEntity<?> transfer(@RequestBody Map<String, Object> request) {
        try {
            accountService.transfer(
                (String) request.get("fromAccountId"),
                (String) request.get("toAccountId"),
                new BigDecimal(request.get("amount").toString())
            );
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/accounts/{accountId}/transactions")
    public ResponseEntity<List<Transaction>> getTransactionHistory(@PathVariable String accountId) {
        List<Transaction> transactions = accountService.getTransactionHistory(accountId);
        return ResponseEntity.ok(transactions);
    }

    private String getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userService.findUserByEmail(auth.getName()).getId();
    }
    
    @PostMapping("/users/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> request) {
        try {
            userService.registerUser(
                request.get("email"),
                request.get("password"),
                request.get("firstName"),
                request.get("lastName")
            );
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (RuntimeException e) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
