package com.Legacy.LegacyBank.Controller;

import com.Legacy.LegacyBank.Model.Account;
import com.Legacy.LegacyBank.Model.AccountType;
import com.Legacy.LegacyBank.Model.Transaction;
import com.Legacy.LegacyBank.Service.AccountService;
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
@RequestMapping("/api/accounts")
public class AccountController {
    
    @Autowired
    private AccountService accountService;
    
    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccount(@PathVariable String id) {
        try {
            Account account = accountService.findAccountById(id);
            return ResponseEntity.ok(account);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping
    public ResponseEntity<?> createAccount(@RequestBody Map<String, Object> request) {
        try {
            Account account = accountService.createAccount(
                (String) request.get("userId"),
                AccountType.valueOf((String) request.get("type"))
            );
            return new ResponseEntity<>(account, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity
                .badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/{id}/transactions")
    public ResponseEntity<?> getTransactions(@PathVariable String id) {
        try {
            List<Transaction> transactions = accountService.getTransactionHistory(id);
            return ResponseEntity.ok(transactions);
        } catch (RuntimeException e) {
            return ResponseEntity
                .badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    @PostMapping("/{id}/deposit")
    public ResponseEntity<?> deposit(
            @PathVariable String id,
            @RequestBody Map<String, BigDecimal> request) {
        try {
            accountService.deposit(id, request.get("amount"));
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity
                .badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    @PostMapping("/{id}/withdraw")
    public ResponseEntity<?> withdraw(
            @PathVariable String id,
            @RequestBody Map<String, BigDecimal> request) {
        try {
            accountService.withdraw(id, request.get("amount"));
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity
                .badRequest()
                .body(Map.of("error", e.getMessage()));
        }
    }
    
    @PostMapping("/transfer")
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
}