package com.zidio.keystones.controller;

import com.zidio.keystones.dto.CreatePartDTO;
import com.zidio.keystones.dto.PartDTO;
import com.zidio.keystones.service.PartService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/parts")
public class PartController {

    private final PartService partService;

    public PartController(PartService partService) {
        this.partService = partService;
    }

    @GetMapping
    public ResponseEntity<Page<PartDTO>> getParts(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        Page<PartDTO> result = partService.getParts(search, PageRequest.of(page, size, Sort.by("name").ascending()));
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PartDTO> getPartById(@PathVariable Long id) {
        return ResponseEntity.ok(partService.getPartById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ResponseEntity<PartDTO> createPart(@Valid @RequestBody CreatePartDTO dto) {
        PartDTO created = partService.createPart(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ResponseEntity<PartDTO> updatePart(
            @PathVariable Long id,
            @Valid @RequestBody CreatePartDTO dto) {
        return ResponseEntity.ok(partService.updatePart(id, dto));
    }
}
