package com.zidio.keystones.service;

import com.zidio.keystones.dto.*;
import com.zidio.keystones.entity.*;
import com.zidio.keystones.enums.*;
import com.zidio.keystones.exception.IllegalStateTransitionException;
import com.zidio.keystones.exception.InsufficientStockException;
import com.zidio.keystones.exception.ResourceNotFoundException;
import com.zidio.keystones.exception.UnauthorizedAccessException;
import com.zidio.keystones.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class WorkOrderService {

    private final WorkOrderRepository workOrderRepository;
    private final CustomerRepository customerRepository;
    private final SiteRepository siteRepository;
    private final UserAuthRepo userAuthRepository;
    private final WorkOrderStatusHistoryRepository historyRepository;
    private final PartRepository partRepository;
    private final PartUsageRepository partUsageRepository;
    private final TimeLogRepository timeLogRepository;

    public WorkOrderService(
            WorkOrderRepository workOrderRepository,
            CustomerRepository customerRepository,
            SiteRepository siteRepository,
            UserAuthRepo userAuthRepository,
            WorkOrderStatusHistoryRepository historyRepository,
            PartRepository partRepository,
            PartUsageRepository partUsageRepository,
            TimeLogRepository timeLogRepository) {
        this.workOrderRepository = workOrderRepository;
        this.customerRepository = customerRepository;
        this.siteRepository = siteRepository;
        this.userAuthRepository = userAuthRepository;
        this.historyRepository = historyRepository;
        this.partRepository = partRepository;
        this.partUsageRepository = partUsageRepository;
        this.timeLogRepository = timeLogRepository;
    }

    @Transactional(readOnly = true)
    public Page<WorkOrderDTO> getWorkOrders(
            UserAuth currentUser,
            Long customerId,
            Long siteId,
            Long technicianId,
            WorkOrderStatus status,
            Priority priority,
            String search,
            Pageable pageable) {

        if (currentUser.getRole() == Role.CUSTOMER) {
            if (currentUser.getCustomer() == null) {
                throw new UnauthorizedAccessException("Customer user has no associated customer organization");
            }
            customerId = currentUser.getCustomer().getId();
        } else if (currentUser.getRole() == Role.TECHNICIAN) {
            if (technicianId == null) {
                technicianId = currentUser.getId();
            }
        }

        return workOrderRepository.findFilteredWorkOrders(customerId, siteId, technicianId, status, priority, search, pageable)
                .map(this::mapToDTO);
    }

    @Transactional(readOnly = true)
    public WorkOrderDTO getWorkOrderById(Long id, UserAuth currentUser) {
        WorkOrder workOrder = workOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Work order not found with id: " + id));

        validateCustomerAccess(currentUser, workOrder);

        return mapToDTO(workOrder);
    }

    @Transactional
    public WorkOrderDTO createWorkOrder(CreateWorkOrderDTO dto, UserAuth currentUser) {
        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + dto.getCustomerId()));

        if (currentUser.getRole() == Role.CUSTOMER) {
            if (currentUser.getCustomer() == null || !currentUser.getCustomer().getId().equals(customer.getId())) {
                throw new UnauthorizedAccessException("Cannot create work orders for another customer organization");
            }
        }

        Site site = siteRepository.findById(dto.getSiteId())
                .orElseThrow(() -> new ResourceNotFoundException("Site not found with id: " + dto.getSiteId()));

        if (!site.getCustomer().getId().equals(customer.getId())) {
            throw new IllegalArgumentException("Site does not belong to the specified customer");
        }

        UserAuth technician = null;
        WorkOrderStatus initialStatus = WorkOrderStatus.NEW;

        if (dto.getAssignedToId() != null) {
            technician = userAuthRepository.findById(dto.getAssignedToId())
                    .orElseThrow(() -> new ResourceNotFoundException("Technician not found with id: " + dto.getAssignedToId()));
            if (technician.getRole() != Role.TECHNICIAN) {
                throw new IllegalArgumentException("Assigned user must have TECHNICIAN role");
            }
            initialStatus = WorkOrderStatus.ASSIGNED;
        }

        LocalDateTime slaDueDate = calculateSlaDueDate(dto.getPriority());

        WorkOrder workOrder = WorkOrder.builder()
                .code(generateUniqueCode())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .priority(dto.getPriority())
                .status(initialStatus)
                .customer(customer)
                .site(site)
                .assignedTo(technician)
                .slaDueDate(slaDueDate)
                .slaBreached(false)
                .build();

        WorkOrder saved = workOrderRepository.save(workOrder);

        recordStatusHistory(saved, null, initialStatus, currentUser.getEmail(), "Work order raised");

        return mapToDTO(saved);
    }

    @Transactional
    public WorkOrderDTO updateWorkOrder(Long id, UpdateWorkOrderDTO dto, UserAuth currentUser) {
        WorkOrder workOrder = workOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Work order not found with id: " + id));

        validateCustomerAccess(currentUser, workOrder);

        if (workOrder.getStatus() == WorkOrderStatus.CLOSED || workOrder.getStatus() == WorkOrderStatus.CANCELLED) {
            throw new IllegalStateTransitionException("Work order is in terminal state (" + workOrder.getStatus() + ") and cannot be edited");
        }

        if (dto.getTitle() != null && !dto.getTitle().isBlank()) {
            workOrder.setTitle(dto.getTitle());
        }
        if (dto.getDescription() != null) {
            workOrder.setDescription(dto.getDescription());
        }
        if (dto.getPriority() != null) {
            workOrder.setPriority(dto.getPriority());
            workOrder.setSlaDueDate(calculateSlaDueDate(dto.getPriority()));
        }

        WorkOrder updated = workOrderRepository.save(workOrder);
        return mapToDTO(updated);
    }

    @Transactional
    public WorkOrderDTO assignTechnician(Long id, AssignWorkOrderDTO dto, UserAuth currentUser) {
        WorkOrder workOrder = workOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Work order not found with id: " + id));

        if (currentUser.getRole() == Role.TECHNICIAN || currentUser.getRole() == Role.CUSTOMER) {
            throw new UnauthorizedAccessException("Only dispatchers and managers can assign work orders");
        }

        if (workOrder.getStatus() == WorkOrderStatus.CLOSED || workOrder.getStatus() == WorkOrderStatus.CANCELLED) {
            throw new IllegalStateTransitionException("Cannot assign work order in terminal state: " + workOrder.getStatus());
        }

        UserAuth technician = userAuthRepository.findById(dto.getTechnicianId())
                .orElseThrow(() -> new ResourceNotFoundException("Technician not found with id: " + dto.getTechnicianId()));

        if (technician.getRole() != Role.TECHNICIAN) {
            throw new IllegalArgumentException("User must have TECHNICIAN role");
        }

        WorkOrderStatus fromStatus = workOrder.getStatus();
        WorkOrderStatus toStatus = WorkOrderStatus.ASSIGNED;

        workOrder.setAssignedTo(technician);
        workOrder.setStatus(toStatus);
        WorkOrder saved = workOrderRepository.save(workOrder);

        String note = dto.getNote() != null ? dto.getNote() : "Assigned to technician " + technician.getFirstName() + " " + technician.getLastName();
        recordStatusHistory(saved, fromStatus, toStatus, currentUser.getEmail(), note);

        return mapToDTO(saved);
    }

    @Transactional
    public WorkOrderDTO updateStatus(Long id, UpdateWorkOrderStatusDTO dto, UserAuth currentUser) {
        WorkOrder workOrder = workOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Work order not found with id: " + id));

        validateCustomerAccess(currentUser, workOrder);

        WorkOrderStatus currentStatus = workOrder.getStatus();
        WorkOrderStatus targetStatus = dto.getStatus();

        validateStateTransition(currentStatus, targetStatus);

        validateRoleForTransition(currentUser, workOrder, targetStatus);

        workOrder.setStatus(targetStatus);
        WorkOrder saved = workOrderRepository.save(workOrder);

        String note = dto.getNote() != null ? dto.getNote() : "Status changed to " + targetStatus;
        recordStatusHistory(saved, currentStatus, targetStatus, currentUser.getEmail(), note);

        return mapToDTO(saved);
    }

    @Transactional
    public PartUsageDTO logPartUsage(Long workOrderId, LogPartUsageDTO dto, UserAuth currentUser) {
        WorkOrder workOrder = workOrderRepository.findById(workOrderId)
                .orElseThrow(() -> new ResourceNotFoundException("Work order not found with id: " + workOrderId));

        if (workOrder.getStatus() == WorkOrderStatus.CLOSED || workOrder.getStatus() == WorkOrderStatus.CANCELLED) {
            throw new IllegalStateTransitionException("Cannot log parts on a closed or cancelled work order");
        }

        Part part = partRepository.findById(dto.getPartId())
                .orElseThrow(() -> new ResourceNotFoundException("Part not found with id: " + dto.getPartId()));

        if (part.getStockQty() < dto.getQtyUsed()) {
            throw new InsufficientStockException("Insufficient stock for part '" + part.getName() + "'. Available: " + part.getStockQty() + ", Requested: " + dto.getQtyUsed());
        }

        part.setStockQty(part.getStockQty() - dto.getQtyUsed());
        partRepository.save(part);

        PartUsage partUsage = PartUsage.builder()
                .workOrder(workOrder)
                .part(part)
                .qtyUsed(dto.getQtyUsed())
                .unitCost(part.getUnitCost())
                .build();

        PartUsage saved = partUsageRepository.save(partUsage);

        return mapToPartUsageDTO(saved);
    }

    @Transactional
    public TimeLogDTO logTime(Long workOrderId, LogTimeDTO dto, UserAuth currentUser) {
        WorkOrder workOrder = workOrderRepository.findById(workOrderId)
                .orElseThrow(() -> new ResourceNotFoundException("Work order not found with id: " + workOrderId));

        if (workOrder.getStatus() == WorkOrderStatus.CLOSED || workOrder.getStatus() == WorkOrderStatus.CANCELLED) {
            throw new IllegalStateTransitionException("Cannot log time on a closed or cancelled work order");
        }

        UserAuth technician = currentUser;
        if (currentUser.getRole() != Role.TECHNICIAN) {
            if (workOrder.getAssignedTo() != null) {
                technician = workOrder.getAssignedTo();
            }
        }

        TimeLog timeLog = TimeLog.builder()
                .workOrder(workOrder)
                .technician(technician)
                .minutes(dto.getMinutes())
                .note(dto.getNote())
                .build();

        TimeLog saved = timeLogRepository.save(timeLog);

        return mapToTimeLogDTO(saved);
    }

    private void validateStateTransition(WorkOrderStatus current, WorkOrderStatus target) {
        if (current == target) {
            return;
        }

        Set<WorkOrderStatus> allowedTargets = switch (current) {
            case NEW -> Set.of(WorkOrderStatus.ASSIGNED, WorkOrderStatus.CANCELLED);
            case ASSIGNED -> Set.of(WorkOrderStatus.IN_PROGRESS, WorkOrderStatus.ASSIGNED, WorkOrderStatus.CANCELLED);
            case IN_PROGRESS -> Set.of(WorkOrderStatus.ON_HOLD, WorkOrderStatus.COMPLETED, WorkOrderStatus.CANCELLED);
            case ON_HOLD -> Set.of(WorkOrderStatus.IN_PROGRESS, WorkOrderStatus.CANCELLED);
            case COMPLETED -> Set.of(WorkOrderStatus.CLOSED);
            case CLOSED, CANCELLED -> Set.of();
        };

        if (!allowedTargets.contains(target)) {
            throw new IllegalStateTransitionException("Illegal status transition from " + current + " to " + target);
        }
    }

    private void validateRoleForTransition(UserAuth currentUser, WorkOrder workOrder, WorkOrderStatus targetStatus) {
        Role role = currentUser.getRole();

        if (targetStatus == WorkOrderStatus.CLOSED && role != Role.MANAGER && role != Role.ADMIN) {
            throw new UnauthorizedAccessException("Only managers can close work orders");
        }

        if (role == Role.TECHNICIAN) {
            if (workOrder.getAssignedTo() == null || !workOrder.getAssignedTo().getId().equals(currentUser.getId())) {
                throw new UnauthorizedAccessException("Technicians can only update status for jobs assigned to them");
            }
            if (targetStatus == WorkOrderStatus.CLOSED || targetStatus == WorkOrderStatus.CANCELLED) {
                throw new UnauthorizedAccessException("Technicians are not allowed to close or cancel jobs");
            }
        }

        if (role == Role.CUSTOMER) {
            if (targetStatus != WorkOrderStatus.CANCELLED) {
                throw new UnauthorizedAccessException("Customers are only allowed to cancel open requests");
            }
        }
    }

    private void validateCustomerAccess(UserAuth user, WorkOrder workOrder) {
        if (user.getRole() == Role.CUSTOMER) {
            if (user.getCustomer() == null || !user.getCustomer().getId().equals(workOrder.getCustomer().getId())) {
                throw new UnauthorizedAccessException("Access denied to work order belonging to another customer");
            }
        }
    }

    private LocalDateTime calculateSlaDueDate(Priority priority) {
        LocalDateTime now = LocalDateTime.now();
        return switch (priority) {
            case URGENT -> now.plusHours(4);
            case HIGH -> now.plusHours(24);
            case MEDIUM -> now.plusHours(48);
            case LOW -> now.plusHours(72);
        };
    }

    private String generateUniqueCode() {
        long count = workOrderRepository.count() + 1;
        String code;
        do {
            code = String.format("WO-2026-%04d", count++);
        } while (workOrderRepository.findByCode(code).isPresent());
        return code;
    }

    private void recordStatusHistory(WorkOrder workOrder, WorkOrderStatus from, WorkOrderStatus to, String changedBy, String note) {
        WorkOrderStatusHistory history = WorkOrderStatusHistory.builder()
                .workOrder(workOrder)
                .fromStatus(from)
                .toStatus(to)
                .changedBy(changedBy)
                .note(note)
                .build();
        historyRepository.save(history);
    }

    public WorkOrderDTO mapToDTO(WorkOrder workOrder) {
        List<WorkOrderStatusHistoryDTO> history = historyRepository
                .findByWorkOrderIdOrderByChangedAtAsc(workOrder.getId()).stream()
                .map(h -> WorkOrderStatusHistoryDTO.builder()
                        .id(h.getId())
                        .workOrderId(h.getWorkOrder().getId())
                        .fromStatus(h.getFromStatus())
                        .toStatus(h.getToStatus())
                        .changedBy(h.getChangedBy())
                        .changedAt(h.getChangedAt())
                        .note(h.getNote())
                        .build())
                .collect(Collectors.toList());

        List<PartUsageDTO> partsUsed = partUsageRepository
                .findByWorkOrderIdOrderByUsedAtAsc(workOrder.getId()).stream()
                .map(this::mapToPartUsageDTO)
                .collect(Collectors.toList());

        List<TimeLogDTO> timeLogs = timeLogRepository
                .findByWorkOrderIdOrderByLoggedAtAsc(workOrder.getId()).stream()
                .map(this::mapToTimeLogDTO)
                .collect(Collectors.toList());

        BigDecimal totalPartsCost = partUsageRepository.calculateTotalPartsCostByWorkOrder(workOrder.getId());
        Integer totalTimeMinutes = timeLogRepository.calculateTotalMinutesByWorkOrder(workOrder.getId());

        SlaStatus slaStatus = calculateSlaStatus(workOrder);

        return WorkOrderDTO.builder()
                .id(workOrder.getId())
                .code(workOrder.getCode())
                .title(workOrder.getTitle())
                .description(workOrder.getDescription())
                .priority(workOrder.getPriority())
                .status(workOrder.getStatus())
                .customerId(workOrder.getCustomer().getId())
                .customerName(workOrder.getCustomer().getName())
                .siteId(workOrder.getSite().getId())
                .siteName(workOrder.getSite().getName())
                .assignedToId(workOrder.getAssignedTo() != null ? workOrder.getAssignedTo().getId() : null)
                .assignedToName(workOrder.getAssignedTo() != null ? workOrder.getAssignedTo().getFirstName() + " " + workOrder.getAssignedTo().getLastName() : "Unassigned")
                .slaDueDate(workOrder.getSlaDueDate())
                .slaBreached(workOrder.getSlaBreached())
                .slaStatus(slaStatus)
                .createdAt(workOrder.getCreatedAt())
                .updatedAt(workOrder.getUpdatedAt())
                .totalPartsCost(totalPartsCost)
                .totalTimeMinutes(totalTimeMinutes)
                .history(history)
                .partsUsed(partsUsed)
                .timeLogs(timeLogs)
                .build();
    }

    private SlaStatus calculateSlaStatus(WorkOrder workOrder) {
        if (workOrder.getStatus() == WorkOrderStatus.COMPLETED || workOrder.getStatus() == WorkOrderStatus.CLOSED) {
            return workOrder.getSlaBreached() != null && workOrder.getSlaBreached() ? SlaStatus.IN_BREACH : SlaStatus.MET;
        }
        if (workOrder.getSlaDueDate() == null) {
            return SlaStatus.PENDING;
        }
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(workOrder.getSlaDueDate()) || (workOrder.getSlaBreached() != null && workOrder.getSlaBreached())) {
            return SlaStatus.IN_BREACH;
        }
        if (now.plusHours(2).isAfter(workOrder.getSlaDueDate())) {
            return SlaStatus.AT_RISK;
        }
        return SlaStatus.PENDING;
    }

    private PartUsageDTO mapToPartUsageDTO(PartUsage pu) {
        BigDecimal totalCost = pu.getUnitCost().multiply(BigDecimal.valueOf(pu.getQtyUsed()));
        return PartUsageDTO.builder()
                .id(pu.getId())
                .workOrderId(pu.getWorkOrder().getId())
                .partId(pu.getPart().getId())
                .partName(pu.getPart().getName())
                .sku(pu.getPart().getSku())
                .qtyUsed(pu.getQtyUsed())
                .unitCost(pu.getUnitCost())
                .totalCost(totalCost)
                .usedAt(pu.getUsedAt())
                .build();
    }

    private TimeLogDTO mapToTimeLogDTO(TimeLog tl) {
        return TimeLogDTO.builder()
                .id(tl.getId())
                .workOrderId(tl.getWorkOrder().getId())
                .technicianId(tl.getTechnician().getId())
                .technicianName(tl.getTechnician().getFirstName() + " " + tl.getTechnician().getLastName())
                .minutes(tl.getMinutes())
                .note(tl.getNote())
                .loggedAt(tl.getLoggedAt())
                .build();
    }
}
