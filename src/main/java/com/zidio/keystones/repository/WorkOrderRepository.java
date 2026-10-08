package com.zidio.keystones.repository;

import com.zidio.keystones.entity.WorkOrder;
import com.zidio.keystones.enums.Priority;
import com.zidio.keystones.enums.WorkOrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface WorkOrderRepository extends JpaRepository<WorkOrder, Long> {
    Optional<WorkOrder> findByCode(String code);

    @Query("SELECT w FROM WorkOrder w WHERE " +
           "(:customerId IS NULL OR w.customer.id = :customerId) AND " +
           "(:siteId IS NULL OR w.site.id = :siteId) AND " +
           "(:technicianId IS NULL OR w.assignedTo.id = :technicianId) AND " +
           "(:status IS NULL OR w.status = :status) AND " +
           "(:priority IS NULL OR w.priority = :priority) AND " +
           "(:search IS NULL OR LOWER(w.code) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(w.title) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(w.description) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<WorkOrder> findFilteredWorkOrders(
            @Param("customerId") Long customerId,
            @Param("siteId") Long siteId,
            @Param("technicianId") Long technicianId,
            @Param("status") WorkOrderStatus status,
            @Param("priority") Priority priority,
            @Param("search") String search,
            Pageable pageable
    );

    List<WorkOrder> findByCustomerIdOrderByCreatedAtDesc(Long customerId);

    List<WorkOrder> findByAssignedToIdOrderByCreatedAtDesc(Long technicianId);

    @Query("SELECT w FROM WorkOrder w WHERE w.status NOT IN ('COMPLETED', 'CLOSED', 'CANCELLED') AND w.slaDueDate < :now")
    List<WorkOrder> findOverdueWorkOrders(@Param("now") LocalDateTime now);

    @Query("SELECT COUNT(w) FROM WorkOrder w WHERE w.status = :status")
    long countByStatus(@Param("status") WorkOrderStatus status);

    @Query("SELECT COUNT(w) FROM WorkOrder w WHERE w.slaBreached = true")
    long countSlaBreached();
}
