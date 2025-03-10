package com.Legacy.LegacyBank.Repository;

import com.Legacy.LegacyBank.Model.Notification;
import com.Legacy.LegacyBank.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserId(Long id);
    
    List<Notification> findByUserAndIsRead(User user, boolean isRead);
}