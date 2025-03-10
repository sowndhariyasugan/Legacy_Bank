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
    private Long id;

    @Column(nullable = false)
    private String type; // Visa, Mastercard, etc.

    @Column(nullable = false)
    private String number; // Masked card number

    @Column(nullable = false)
    private String expiry;

    @Column(nullable = false)
    private String bank;

    private String color;

    @Column(name = "annual_fee")
    private String annualFee;

    @Column(name = "cashback_rate")
    private String cashbackRate;

    private String rewards;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}