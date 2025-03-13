package com.Legacy.LegacyBank.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "settings")
public class Settings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "two_factor")
    private Boolean twoFactor = false;
    
    @Column(name = "login_notifications")
    private Boolean loginNotifications = false;
    
    @Column(name = "email_notifications")
    private Boolean emailNotifications = false;
    
    @Column(name = "sms_notifications")
    private Boolean smsNotifications = false;
    
    @Column(name = "transaction_notifications")
    private Boolean transactionNotifications = false;
    
    @Column(name = "marketing_notifications")
    private Boolean marketingNotifications = false;
    
    @Column(name = "show_balance")
    private Boolean showBalance = true;
    
    @Column(name = "activity_tracking")
    private Boolean activityTracking = true;
    
    @Column(name = "data_sharing")
    private Boolean dataSharing = false;
    
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
} 