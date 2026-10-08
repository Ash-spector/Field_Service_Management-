package com.zidio.keystones.controller;

import com.zidio.keystones.dto.*;
import com.zidio.keystones.entity.UserAuth;
import com.zidio.keystones.enums.Priority;
import com.zidio.keystones.enums.WorkOrderStatus;
import com.zidio.keystones.service.WorkOrderService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/work-orders")
public class WorkOrderController {

    private final WorkOrderService workOrderService;

    public WorkOrderController(WorkOrderService workOrderService) {
        this.workOrderService = workOrderService;
    }

    @GetMapping
    public ResponseEntity<Page<WorkOrderDTO>> getWorkOrders(
            @AuthenticationPrincipal UserAuth currentUser,
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) Long siteId,
            @RequestParam(required = false) Long technicianId,
            @RequestParam(required = false) WorkOrderStatus status,
            @RequestParam(required = false) Priority priority,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<WorkOrderDTO> result = workOrderService.getWorkOrders(
                currentUser, customerId, siteId, technicianId, status, priority, search,
                PageRequest.of(page, size, Sort.by("id").descending())
        );
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkOrderDTO> getWorkOrderById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserAuth currentUser) {
        return ResponseEntity.ok(workOrderService.getWorkOrderById(id, currentUser));
    }

    @PostMapping
    public ResponseEntity<WorkOrderDTO> createWorkOrder(
            @Valid @RequestBody CreateWorkOrderDTO dto,
            @AuthenticationPrincipal UserAuth currentUser) {
        WorkOrderDTO created = workOrderService.createWorkOrder(dto, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkOrderDTO> updateWorkOrder(
            @PathVariable Long id,
            @RequestBody UpdateWorkOrderDTO dto,
            @AuthenticationPrincipal UserAuth currentUser) {
        return ResponseEntity.ok(workOrderService.updateWorkOrder(id, dto, currentUser));
    }

    @PostMapping("/{id}/assign")
    public ResponseEntity<WorkOrderDTO> assignTechnician(
            @PathVariable Long id,
            @Valid @RequestBody AssignWorkOrderDTO dto,
            @AuthenticationPrincipal UserAuth currentUser) {
        return ResponseEntity.ok(workOrderService.assignTechnician(id, dto, currentUser));
    }

    @PostMapping("/{id}/status")
    public ResponseEntity<WorkOrderDTO> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateWorkOrderStatusDTO dto,
            @AuthenticationPrincipal UserAuth currentUser) {
        return ResponseEntity.ok(workOrderService.updateStatus(id, dto, currentUser));
    }

    @PostMapping("/{id}/parts")
    public ResponseEntity<PartUsageDTO> logPartUsage(
            @PathVariable Long id,
            @Valid @RequestBody LogPartUsageDTO dto,
            @AuthenticationPrincipal UserAuth currentUser) {
        PartUsageDTO result = workOrderService.logPartUsage(id, dto, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PostMapping("/{id}/time")
    public ResponseEntity<TimeLogDTO> logTime(
            @PathVariable Long id,
            @Valid @RequestBody LogTimeDTO dto,
            @AuthenticationPrincipal UserAuth currentUser) {
        TimeLogDTO result = workOrderService.logTime(id, dto, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}
