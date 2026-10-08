package com.zidio.keystones.service;

import com.zidio.keystones.dto.CreatePartDTO;
import com.zidio.keystones.dto.PartDTO;
import com.zidio.keystones.entity.Part;
import com.zidio.keystones.exception.ResourceNotFoundException;
import com.zidio.keystones.repository.PartRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PartService {

    private final PartRepository partRepository;

    public PartService(PartRepository partRepository) {
        this.partRepository = partRepository;
    }

    @Transactional(readOnly = true)
    public Page<PartDTO> getParts(String search, Pageable pageable) {
        return partRepository.searchParts(search, pageable)
                .map(this::mapToDTO);
    }

    @Transactional(readOnly = true)
    public PartDTO getPartById(Long id) {
        Part part = partRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Part not found with id: " + id));
        return mapToDTO(part);
    }

    @Transactional
    public PartDTO createPart(CreatePartDTO dto) {
        if (partRepository.findBySku(dto.getSku()).isPresent()) {
            throw new IllegalArgumentException("Part with SKU " + dto.getSku() + " already exists");
        }
        Part part = Part.builder()
                .name(dto.getName())
                .sku(dto.getSku())
                .unitCost(dto.getUnitCost())
                .stockQty(dto.getStockQty())
                .build();
        Part saved = partRepository.save(part);
        return mapToDTO(saved);
    }

    @Transactional
    public PartDTO updatePart(Long id, CreatePartDTO dto) {
        Part part = partRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Part not found with id: " + id));
        part.setName(dto.getName());
        part.setSku(dto.getSku());
        part.setUnitCost(dto.getUnitCost());
        part.setStockQty(dto.getStockQty());
        Part updated = partRepository.save(part);
        return mapToDTO(updated);
    }

    private PartDTO mapToDTO(Part part) {
        return PartDTO.builder()
                .id(part.getId())
                .name(part.getName())
                .sku(part.getSku())
                .unitCost(part.getUnitCost())
                .stockQty(part.getStockQty())
                .createdAt(part.getCreatedAt())
                .build();
    }
}
