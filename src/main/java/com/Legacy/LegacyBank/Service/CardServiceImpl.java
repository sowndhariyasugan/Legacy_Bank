package com.Legacy.LegacyBank.Service;

import com.Legacy.LegacyBank.Model.Card;
import com.Legacy.LegacyBank.Model.User;
import com.Legacy.LegacyBank.Model.Account;
import com.Legacy.LegacyBank.Repository.CardRepository;
import com.Legacy.LegacyBank.Repository.UserRepository;
import com.Legacy.LegacyBank.Repository.AccountRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CardServiceImpl implements CardService {

    @Autowired
    private CardRepository cardRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public List<Card> getAllCards() {
        return cardRepository.findAll();
    }
    
    @Override
    public List<Card> getCardsByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        return cardRepository.findByUser(user);
    }

    @Override
    public Optional<Card> getCardById(Long id) {
        return cardRepository.findById(id);
    }
    
    @Override
    @Transactional
    public Card createCard(User user, Account account, String cardType) {
        // Verify that the account belongs to the user
        if (!account.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Account does not belong to the user");
        }
        
        Card card = new Card();
        card.setCardType(cardType);
        card.setCardNumber(generateCardNumber());
        card.setExpiryDate(generateExpiryDate());
        card.setCvv(generateCVV());
        card.setStatus("ACTIVE");
        card.setUser(user);
        card.setAccount(account);
        
        return cardRepository.save(card);
    }
    
    @Override
    public Card updateCard(Long id, Card cardDetails) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Card not found with id: " + id));
        
        if (cardDetails.getStatus() != null) {
            card.setStatus(cardDetails.getStatus());
        }
        
        return cardRepository.save(card);
    }
    
    @Override
    public void deleteCard(Long id) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Card not found with id: " + id));
        
        cardRepository.delete(card);
    }
    
    @Override
    public boolean isCardLinkedToUser(Long cardId, Long userId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found with id: " + cardId));
        
        return card.getUser().getId().equals(userId);
    }
    
    @Override
    public boolean isCardLinkedToAccount(Long cardId, Long accountId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found with id: " + cardId));
        
        return card.getAccount().getId().equals(accountId);
    }
    
    private String generateCardNumber() {
        // Generate a random card number (16 digits)
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }
    
    private String generateExpiryDate() {
        // Generate expiry date 4 years from now
        LocalDate expiryDate = LocalDate.now().plusYears(4);
        return expiryDate.format(DateTimeFormatter.ofPattern("MM/yy"));
    }
    
    private String generateCVV() {
        // Generate a random 3-digit CVV
        return String.format("%03d", (int)(Math.random() * 1000));
    }
} 