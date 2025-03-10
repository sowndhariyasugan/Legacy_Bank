package com.Legacy.LegacyBank.Repository;

import com.Legacy.LegacyBank.Model.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, String> {
    Optional<Card> findByNumber(String number);
    List<Card> findByUserId(String userId);
    boolean existsByNumber(String number);
}