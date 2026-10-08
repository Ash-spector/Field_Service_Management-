package com.zidio.keystones.repository;

import com.zidio.keystones.entity.TimeLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TimeLogRepository extends JpaRepository<TimeLog, Long> {
    List<TimeLog> findByWorkOrderIdOrderByLoggedAtAsc(Long workOrderId);

    @Query("SELECT COALESCE(SUM(tl.minutes), 0) FROM TimeLog tl WHERE tl.workOrder.id = :workOrderId")
    Integer calculateTotalMinutesByWorkOrder(@Param("workOrderId") Long workOrderId);
}
