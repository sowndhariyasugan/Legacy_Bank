package com.Legacy.LegacyBank.Service;

import com.Legacy.LegacyBank.Model.Card;
import com.Legacy.LegacyBank.Model.User;
import com.Legacy.LegacyBank.Model.Account;

import java.util.List;
import java.util.Optional;

public interface CardService {
    List<Card> getAllCards();
    
    List<Card> getCardsByUserId(Long userId);
    
    Optional<Card> getCardById(Long id);
    
    Card createCard(User user, Account account, String cardType);
    
    Card updateCard(Long id, Card cardDetails);
    
    void deleteCard(Long id);
    
    boolean isCardLinkedToUser(Long cardId, Long userId);
    
    boolean isCardLinkedToAccount(Long cardId, Long accountId);
} 