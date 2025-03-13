package com.Legacy.LegacyBank.Controller;

import com.Legacy.LegacyBank.Model.Card;
import com.Legacy.LegacyBank.Model.Account;
import com.Legacy.LegacyBank.Model.User;
import com.Legacy.LegacyBank.Repository.CardRepository;
import com.Legacy.LegacyBank.Repository.AccountRepository;
import com.Legacy.LegacyBank.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    @Autowired
    private CardRepository cardRepository;
    
    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private UserService userService;
    
    @GetMapping
    public ResponseEntity<List<Card>> getAllCards() {
        // Get current authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        
        // Return only the cards belonging to the authenticated user
        List<Card> cards = cardRepository.findByUser(user);
        return ResponseEntity.ok(cards);
    }
    
    @PostMapping
    public ResponseEntity<?> createCard(@Valid @RequestBody Map<String, String> request) {
        try {
            // Get current authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            User user = userService.findByUsername(username);
            
            // Validate input
            String cardType = request.get("cardType");
            if (cardType == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Card type is required"));
            }
            
            Long accountId;
            try {
                accountId = Long.valueOf(request.get("accountId"));
            } catch (NumberFormatException | NullPointerException e) {
                return ResponseEntity.badRequest().body(Map.of("error", "Valid account ID is required"));
            }
            
            // Get the account and verify ownership
            Optional<Account> accountOpt = accountRepository.findById(accountId);
            if (accountOpt.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Account not found"));
            }
            
            Account account = accountOpt.get();
            if (!account.getUser().getId().equals(user.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                       .body(Map.of("error", "You do not have permission to access this account"));
            }
            
            // Create card
            Card card = new Card();
            card.setCardType(cardType);
            card.setCardNumber(generateCardNumber());
            card.setExpiryDate(generateExpiryDate());
            card.setCvv(generateCVV());
            card.setStatus("ACTIVE");
            card.setUser(user);
            card.setAccount(account);
            
            Card savedCard = cardRepository.save(card);
            
            // Mask the card number and CVV for response
            savedCard.setCardNumber(maskCardNumber(savedCard.getCardNumber()));
            savedCard.setCvv("***");
            
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCard);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCard(@PathVariable Long id) {
        try {
            // Get current authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            User user = userService.findByUsername(username);
            
            // Check if the card exists
            Optional<Card> cardOpt = cardRepository.findById(id);
            if (cardOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            // Check if the card belongs to the authenticated user
            Card card = cardOpt.get();
            if (!card.getUser().getId().equals(user.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                       .body(Map.of("error", "You do not have permission to delete this card"));
            }
            
            // Delete the card
            cardRepository.delete(card);
            
            return ResponseEntity.ok(Map.of("message", "Card successfully deleted"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    // Helper methods
    private String generateCardNumber() {
        // Simple implementation for demo purposes
        return "4" + String.format("%015d", (long) (Math.random() * 1000000000000000L));
    }
    
    private String generateExpiryDate() {
        // Simple implementation for demo purposes (current month + 3 years)
        java.time.LocalDate expiryDate = java.time.LocalDate.now().plusYears(3);
        return String.format("%02d/%d", expiryDate.getMonthValue(), expiryDate.getYear() % 100);
    }
    
    private String generateCVV() {
        // Simple implementation for demo purposes
        return String.format("%03d", (int) (Math.random() * 1000));
    }
    
    private String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 8) {
            return cardNumber;
        }
        return "**** **** **** " + cardNumber.substring(cardNumber.length() - 4);
    }
} 