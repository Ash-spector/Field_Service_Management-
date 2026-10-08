package com.zidio.keystones.controller;

import com.zidio.keystones.dto.*;
import com.zidio.keystones.entity.UserAuth;
import com.zidio.keystones.service.UserAuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class UserAuthController {

    private final UserAuthService userService;

    public UserAuthController(UserAuthService userService) {
        this.userService = userService;
    }

    @PostMapping({"/auth/users/register", "/api/auth/register"})
    public ResponseEntity<AuthResponseDTO> register(
            @Valid @RequestBody RegisterRequestDTO register) {
        AuthResponseDTO response = userService.register(register);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping({"/auth/users/login", "/api/auth/login"})
    public ResponseEntity<AuthResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO login) {
        AuthResponseDTO response = userService.login(login);
        return ResponseEntity.ok(response);
    }

    @GetMapping({"/auth/users/me", "/api/auth/me"})
    public ResponseEntity<UserDTO> getCurrentUser(
            @AuthenticationPrincipal UserAuth currentUser) {
        return ResponseEntity.ok(userService.getCurrentUserProfile(currentUser));
    }

    @PostMapping({"/auth/users/forgot-password", "/api/auth/forgot-password"})
    public ResponseEntity<String> forgotPassword(
            @RequestBody ForgotPasswordDTO forgotPasswordDTO) {
        userService.forgotPassword(forgotPasswordDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .body("Reset Password link has been sent to your registered email.");
    }

    @PostMapping({"/auth/users/reset-password", "/api/auth/reset-password"})
    public ResponseEntity<String> resetPassword(
            @RequestBody ResetPasswordDTO resetPasswordDTO) {
        userService.resetPassword(resetPasswordDTO);
        return ResponseEntity.ok("Password reset successfully");
    }

    @PostMapping({"/auth/users/logout", "/api/auth/logout"})
    public ResponseEntity<String> logout(
            @RequestHeader("Authorization") String authHeader) {
        String response = userService.logout(authHeader);
        return ResponseEntity.ok(response);
    }
}