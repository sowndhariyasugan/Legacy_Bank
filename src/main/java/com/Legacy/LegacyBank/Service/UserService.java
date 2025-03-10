package com.Legacy.LegacyBank.Service;

import com.Legacy.LegacyBank.Model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {
    List<User> getAllUsers();
    Optional<User> getUserById(Long id);
    User createUser(User user);
    User updateUser(Long id, User userDetails);
    void deleteUser(Long id);
    
    User findUserByEmail(String email);
    User registerUser(String username, String password);
    User registerUser(String email, String password, String firstName, String lastName);
    boolean existsByEmail(String email);
}