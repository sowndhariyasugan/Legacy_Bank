package com.Legacy.LegacyBank.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @Column(nullable = false)
    private BigDecimal balance;
    
    @Column(nullable = false)
    private String currency;
    
    @Column(nullable = false)
    private String status;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(unique = true)
    private String accountNumber;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "fromAccount", cascade = CascadeType.ALL)
    private Set<Transaction> outgoingTransactions = new HashSet<>();
    
    @OneToMany(mappedBy = "toAccount", cascade = CascadeType.ALL)
    private Set<Transaction> incomingTransactions = new HashSet<>();

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private Set<Card> cards = new HashSet<>();
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}