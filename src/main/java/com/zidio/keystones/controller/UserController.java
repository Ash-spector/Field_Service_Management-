package com.zidio.keystones.controller;

import com.zidio.keystones.dto.UserDTO;
import com.zidio.keystones.entity.UserAuth;
import com.zidio.keystones.enums.Role;
import com.zidio.keystones.repository.UserAuthRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserAuthRepo userAuthRepository;

    public UserController(UserAuthRepo userAuthRepository) {
        this.userAuthRepository = userAuthRepository;
    }

    @GetMapping("/technicians")
    public ResponseEntity<List<UserDTO>> getTechnicians() {
        List<UserDTO> technicians = userAuthRepository.findAll().stream()
                .filter(u -> u.getRole() == Role.TECHNICIAN)
                .map(this::mapToUserDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(technicians);
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userAuthRepository.findAll().stream()
                .map(this::mapToUserDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    private UserDTO mapToUserDTO(UserAuth user) {
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
