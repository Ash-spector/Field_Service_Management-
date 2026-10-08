package com.zidio.keystones.controller;

import com.zidio.keystones.dto.CreateSiteDTO;
import com.zidio.keystones.dto.SiteDTO;
import com.zidio.keystones.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sites")
public class SiteController {

    private final CustomerService customerService;

    public SiteController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<List<SiteDTO>> getAllSites() {
        return ResponseEntity.ok(customerService.getAllSites());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN', 'DISPATCHER')")
    public ResponseEntity<SiteDTO> createSite(@Valid @RequestBody CreateSiteDTO dto) {
        SiteDTO created = customerService.createSite(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
