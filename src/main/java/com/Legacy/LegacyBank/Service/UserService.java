package com.Legacy.LegacyBank.Service;

import com.Legacy.LegacyBank.Model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User findUserByEmail(String email);
    User registerUser(String email, String password, String firstName, String lastName);
    boolean existsByEmail(String email);
}