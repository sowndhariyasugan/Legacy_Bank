package com.Legacy.LegacyBank.Repository;

import com.Legacy.LegacyBank.Model.Settings;
import com.Legacy.LegacyBank.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SettingsRepository extends JpaRepository<Settings, Long> {
    Optional<Settings> findByUser(User user);
} 