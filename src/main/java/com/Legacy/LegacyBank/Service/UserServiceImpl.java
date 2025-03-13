package com.Legacy.LegacyBank.Service;

import com.Legacy.LegacyBank.Model.User;
import com.Legacy.LegacyBank.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public User createUser(User user) {
        // Check if email already exists
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        // Check if username already exists
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        
        // Encode password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User registerUser(String username, String password) {
        // Check if username already exists
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username is already taken!");
        }
        
        // Create new user
        User user = new User();
        user.setUsername(username);
        // Also set the email to the username to avoid null constraint violation
        user.setEmail(username);
        user.setPassword(passwordEncoder.encode(password));
        
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        
        // Update user fields
        if (userDetails.getUsername() != null && !userDetails.getUsername().equals(user.getUsername())) {
            if (userRepository.existsByUsername(userDetails.getUsername())) {
                throw new RuntimeException("Username already exists");
            }
            user.setUsername(userDetails.getUsername());
        }
        
        if (userDetails.getEmail() != null && !userDetails.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(userDetails.getEmail())) {
                throw new RuntimeException("Email already exists");
            }
            user.setEmail(userDetails.getEmail());
        }
        
        if (userDetails.getFirstName() != null) {
            user.setFirstName(userDetails.getFirstName());
        }
        
        if (userDetails.getLastName() != null) {
            user.setLastName(userDetails.getLastName());
        }
        
        if (userDetails.getPhone() != null) {
            user.setPhone(userDetails.getPhone());
        }
        
        if (userDetails.getProfileImage() != null) {
            user.setProfileImage(userDetails.getProfileImage());
        }
        
        // Don't update password here - should be a separate endpoint with proper validation
        
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        
        userRepository.delete(user);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    @Transactional
    public User registerUser(String username, String email, String password, String firstName, String lastName) {
        // Check if username already exists
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }
        
        // Check if email already exists
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already exists");
        }
        
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setFirstName(firstName);
        user.setLastName(lastName);
        
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Try to find by email first (since your frontend is sending email as username)
        try {
            User user = findUserByEmail(username);
            
            return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
            );
        } catch (UsernameNotFoundException e) {
            // If not found by email, try by username as fallback
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with username or email: " + username));
            
            return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
            );
        }
    }
}