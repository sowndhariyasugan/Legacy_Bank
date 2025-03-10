package com.Legacy.LegacyBank.Repository;

import com.Legacy.LegacyBank.Model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, String> {
    Optional<Account> findByNumber(String number);
    List<Account> findByUserId(String userId);
    boolean existsByNumber(String number);
}
