package com.Legacy.LegacyBank.Controller;
import com.Legacy.LegacyBank.Model.Transaction;
import com.Legacy.LegacyBank.Service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping
    public List<Transaction> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable Long id) {
        return transactionService.getTransactionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Transaction createTransaction(@RequestBody Transaction transaction) {
        return transactionService.createTransaction(transaction);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Transaction> updateTransaction(@PathVariable Long id, @RequestBody Transaction transactionDetails) {
        try {
            Transaction updatedTransaction = transactionService.updateTransaction(id, transactionDetails);
            return ResponseEntity.ok(updatedTransaction);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        try {
            transactionService.deleteTransaction(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(@Valid @RequestBody Map<String, Object> request) {
        try {
            Long accountId = Long.valueOf(request.get("accountId").toString());
            BigDecimal amount = new BigDecimal(request.get("amount").toString());
            String description = (String) request.get("description");
            
            Transaction transaction = transactionService.deposit(accountId, amount, description);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Deposit successful");
            response.put("transaction", transaction);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PostMapping("/withdraw")
    public ResponseEntity<?> withdraw(@Valid @RequestBody Map<String, Object> request) {
        try {
            Long accountId = Long.valueOf(request.get("accountId").toString());
            BigDecimal amount = new BigDecimal(request.get("amount").toString());
            String description = (String) request.get("description");
            
            Transaction transaction = transactionService.withdraw(accountId, amount, description);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Withdrawal successful");
            response.put("transaction", transaction);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@Valid @RequestBody Map<String, Object> request) {
        try {
            Long fromAccountId = Long.valueOf(request.get("fromAccountId").toString());
            Long toAccountId = Long.valueOf(request.get("toAccountId").toString());
            BigDecimal amount = new BigDecimal(request.get("amount").toString());
            String description = (String) request.get("description");
            
            Transaction transaction = transactionService.transfer(fromAccountId, toAccountId, amount, description);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Transfer successful");
            response.put("transaction", transaction);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/history")
    public ResponseEntity<Page<Transaction>> getTransactionHistory(
            @RequestParam(required = false) Long accountId,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status,
            Pageable pageable) {
        
        Page<Transaction> transactions = transactionService.getTransactionHistory(accountId, type, status, pageable);
        return ResponseEntity.ok(transactions);
    }
}