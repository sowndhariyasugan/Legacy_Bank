package com.Legacy.LegacyBank.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cards")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_id")
    private Long id;

    @Column(name = "card_type", nullable = false)
    private String cardType;

    @Column(name = "card_number", nullable = false)
    private String cardNumber;

    @Column(name = "expiry_date", nullable = false)
    private String expiryDate;

    @Column(nullable = false)
    private String cvv;

    @Column(nullable = false)
    private String status;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;
}