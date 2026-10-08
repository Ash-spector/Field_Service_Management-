package com.zidio.keystones.service;

import com.zidio.keystones.dto.*;
import com.zidio.keystones.entity.UserAuth;
import com.zidio.keystones.repository.UserAuthRepo;
import com.zidio.keystones.security.JWTTokenUtil;
import com.zidio.keystones.security.TokenKillService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserAuthService {

    private final UserAuthRepo repo;
    private final PasswordEncoder passwordEncoder;
    private final JWTTokenUtil jwtUtil;
    private final EmailService emailService;
    private final TokenKillService tokenKillService;

    public UserAuthService(
            UserAuthRepo repo,
            PasswordEncoder passwordEncoder,
            JWTTokenUtil jwtUtil,
            EmailService emailService,
            TokenKillService tokenKillService) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.emailService = emailService;
        this.tokenKillService = tokenKillService;
    }

    public AuthResponseDTO register(RegisterRequestDTO register) {

        Optional<UserAuth> existingUser =
                repo.findByEmail(register.getEmail());

        if (existingUser.isPresent()) {
            throw new RuntimeException("User already exists with email: " + register.getEmail());
        }

        UserAuth user = new UserAuth();

        user.setFirstName(register.getFirstName());
        user.setLastName(register.getLastName());
        user.setEmail(register.getEmail());
        user.setPassword(
                passwordEncoder.encode(register.getPassword())
        );
        user.setPhone(register.getPhone());
        user.setRole(register.getRole());

        UserAuth saved = repo.save(user);

        String token = jwtUtil.generateToken(saved);

        return AuthResponseDTO.builder()
                .token(token)
                .message("User registered successfully")
                .user(mapToUserDTO(saved))
                .build();
    }

    public AuthResponseDTO login(LoginRequestDTO login) {

        UserAuth user = repo.findByEmail(login.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(login.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtUtil.generateToken(user);

        return AuthResponseDTO.builder()
                .token(token)
                .message("User logged in successfully")
                .user(mapToUserDTO(user))
                .build();
    }

    public UserDTO getCurrentUserProfile(UserAuth user) {
        if (user == null) {
            throw new RuntimeException("Unauthenticated user");
        }
        UserAuth freshUser = repo.findById(user.getId())
                .orElse(user);
        return mapToUserDTO(freshUser);
    }

    public void forgotPassword(ForgotPasswordDTO forgotPassword) {

        UserAuth user = repo.findByEmail(forgotPassword.getEmail())
                .orElseThrow(() -> new RuntimeException("User Not Exist"));

        String token = UUID.randomUUID().toString();

        user.setResetToken(token);
        user.setResetTokenExpiry(
                new Date(System.currentTimeMillis() + (1000 * 60 * 10))
        );

        repo.save(user);

        String resetLink =
                "http://localhost:8080/auth/users/reset-password?token=" + token;

        try {
            emailService.sendResetPasswordLink(user.getEmail(), resetLink);
        } catch (Exception e) {
            System.err.println("Failed to send reset email: " + e.getMessage());
        }
    }

    public void resetPassword(ResetPasswordDTO resetPassword) {

        UserAuth user = repo.findByResetToken(resetPassword.getToken())
                .orElseThrow(() -> new RuntimeException("Invalid Token"));

        if (user.getResetTokenExpiry().before(new Date())) {
            throw new RuntimeException("Reset Password link expired");
        }

        user.setPassword(
                passwordEncoder.encode(resetPassword.getNewPassword())
        );

        user.setResetToken(null);
        user.setResetTokenExpiry(null);

        repo.save(user);
    }

    public String logout(String authHeader) {

        String token = jwtUtil.exractToken(authHeader);

        if (token == null || token.isBlank()) {
            throw new RuntimeException("Authorization token is missing");
        }

        if (!jwtUtil.validateToken(token)) {
            throw new RuntimeException("Invalid or expired token");
        }

        if (tokenKillService.isBlockedToken(token)) {
            return "Token already revoked";
        }

        tokenKillService.blackListToken(token);

        return "Logout successful. Token has been revoked.";
    }

    public UserDTO mapToUserDTO(UserAuth user) {
        return UserDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .customerId(user.getCustomer() != null ? user.getCustomer().getId() : null)
                .customerName(user.getCustomer() != null ? user.getCustomer().getName() : null)
                .build();
    }
}