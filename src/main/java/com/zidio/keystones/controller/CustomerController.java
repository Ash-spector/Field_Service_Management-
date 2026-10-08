package com.zidio.keystones.controller;

import com.zidio.keystones.dto.CreateCustomerDTO;
import com.zidio.keystones.dto.CreateSiteDTO;
import com.zidio.keystones.dto.CustomerDTO;
import com.zidio.keystones.dto.SiteDTO;
import com.zidio.keystones.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<Page<CustomerDTO>> getCustomers(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<CustomerDTO> result = customerService.getCustomers(search, PageRequest.of(page, size, Sort.by("id").descending()));
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN', 'DISPATCHER')")
    public ResponseEntity<CustomerDTO> createCustomer(@Valid @RequestBody CreateCustomerDTO dto) {
        CustomerDTO created = customerService.createCustomer(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN', 'DISPATCHER')")
    public ResponseEntity<CustomerDTO> updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody CreateCustomerDTO dto) {
        return ResponseEntity.ok(customerService.updateCustomer(id, dto));
    }

    @GetMapping("/{id}/sites")
    public ResponseEntity<List<SiteDTO>> getSitesByCustomer(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getSitesByCustomer(id));
    }

    @PostMapping("/{id}/sites")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN', 'DISPATCHER')")
    public ResponseEntity<SiteDTO> createSiteForCustomer(
            @PathVariable Long id,
            @Valid @RequestBody CreateSiteDTO dto) {
        dto.setCustomerId(id);
        SiteDTO created = customerService.createSite(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
