package com.supermarket.management.controller;

import com.supermarket.management.config.JwtTokenProvider;
import com.supermarket.management.model.UserAccount;
import com.supermarket.management.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, 
                          AuthenticationManager authenticationManager, 
                          JwtTokenProvider jwtTokenProvider, 
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserAccount userAccount) {
        if (userAccount.getUsername() == null || userAccount.getUsername().trim().isEmpty() ||
            userAccount.getPassword() == null || userAccount.getPassword().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Username and password are required."));
        }

        String username = userAccount.getUsername().toLowerCase().trim();
        if (username.equals("admin") || username.equals("cashier")) {
            return ResponseEntity.badRequest().body(Map.of("message", "Username is already taken."));
        }

        Optional<UserAccount> existing = userRepository.findByUsername(username);
        if (existing.isPresent()) {
            System.out.println("AuthController: User " + username + " already exists. Overwriting credentials.");
            UserAccount accountToUpdate = existing.get();
            accountToUpdate.setPassword(passwordEncoder.encode(userAccount.getPassword()));
            accountToUpdate.setBusinessName(userAccount.getBusinessName());
            UserAccount saved = userRepository.save(accountToUpdate);
            return ResponseEntity.ok(saved);
        }

        userAccount.setUsername(username);
        userAccount.setPassword(passwordEncoder.encode(userAccount.getPassword()));
        userAccount.setRole("admin"); // Registering businesses defaults to admin role
        UserAccount saved = userRepository.save(userAccount);
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        System.out.println("AuthController: Login attempt for username: " + username);

        if (username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Username and password are required."));
        }

        final String finalUsername = username.toLowerCase().trim();

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(finalUsername, password)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserAccount account = userRepository.findByUsername(finalUsername)
                    .orElseThrow(() -> new RuntimeException("User account not found after authentication: " + finalUsername));

            String token = jwtTokenProvider.createToken(account.getUsername(), account.getRole(), account.getBusinessName());

            System.out.println("AuthController: Successful login for user: " + username);
            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "username", account.getUsername(),
                    "role", account.getRole(),
                    "businessName", account.getBusinessName()
            ));
        } catch (Exception e) {
            System.out.println("AuthController: Authentication failed for user: " + username + ". Reason: " + e.getMessage());
            return ResponseEntity.status(401).body(Map.of("message", "Invalid username or password."));
        }
    }
}
