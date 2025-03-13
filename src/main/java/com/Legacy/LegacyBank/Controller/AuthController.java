package com.Legacy.LegacyBank.Controller;

import com.Legacy.LegacyBank.Model.User;
import com.Legacy.LegacyBank.Service.JwtService;
import com.Legacy.LegacyBank.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody Map<String, String> loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.get("username"),
                            loginRequest.get("password")
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwt = jwtService.generateToken(userDetails);

            Map<String, Object> response = new HashMap<>();
            response.put("token", jwt);
            response.put("tokenType", "Bearer");
            response.put("username", userDetails.getUsername());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Invalid username or password"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody Map<String, String> registerRequest) {
        try {
            // Check if username exists
            if (userService.existsByUsername(registerRequest.get("username"))) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Username is already taken"));
            }

            // Check if email exists
            if (userService.existsByEmail(registerRequest.get("email"))) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Email is already in use"));
            }

            User user = userService.registerUser(
                    registerRequest.get("username"),
                    registerRequest.get("email"),
                    registerRequest.get("password"),
                    registerRequest.get("firstName"),
                    registerRequest.get("lastName")
            );

            Map<String, Object> response = new HashMap<>();
            response.put("message", "User registered successfully");
            response.put("userId", user.getId());
            response.put("username", user.getUsername());

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }
}