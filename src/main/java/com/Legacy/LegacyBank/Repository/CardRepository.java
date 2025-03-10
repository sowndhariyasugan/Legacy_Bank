package com.Legacy.LegacyBank.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Legacy.LegacyBank.Model.Card;
import com.Legacy.LegacyBank.Model.User;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findByUser(User user);
}