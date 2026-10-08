package com.zidio.keystones.repository;

import com.zidio.keystones.entity.PartUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface PartUsageRepository extends JpaRepository<PartUsage, Long> {
    List<PartUsage> findByWorkOrderIdOrderByUsedAtAsc(Long workOrderId);

    @Query("SELECT COALESCE(SUM(pu.qtyUsed * pu.unitCost), 0) FROM PartUsage pu WHERE pu.workOrder.id = :workOrderId")
    BigDecimal calculateTotalPartsCostByWorkOrder(@Param("workOrderId") Long workOrderId);
}
