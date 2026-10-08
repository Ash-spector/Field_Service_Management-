package com.zidio.keystones.service;

import com.zidio.keystones.dto.DashboardReportDTO;
import com.zidio.keystones.entity.WorkOrder;
import com.zidio.keystones.enums.WorkOrderStatus;
import com.zidio.keystones.repository.WorkOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardService {

    private final WorkOrderRepository workOrderRepository;

    public DashboardService(WorkOrderRepository workOrderRepository) {
        this.workOrderRepository = workOrderRepository;
    }

    @Transactional(readOnly = true)
    public DashboardReportDTO getDashboardMetrics() {
        List<WorkOrder> allOrders = workOrderRepository.findAll();
        long totalWorkOrders = allOrders.size();

        Map<String, Long> statusCounts = new HashMap<>();
        Map<String, Long> priorityCounts = new HashMap<>();
        Map<String, Long> technicianWorkload = new HashMap<>();
        Map<String, Long> siteBreakdown = new HashMap<>();

        long activeCount = 0;
        long overdueCount = 0;
        long slaBreachedCount = 0;
        long totalFinished = 0;
        long slaMetFinished = 0;

        LocalDateTime now = LocalDateTime.now();

        for (WorkOrder wo : allOrders) {
            String statusStr = wo.getStatus().name();
            statusCounts.put(statusStr, statusCounts.getOrDefault(statusStr, 0L) + 1);

            String priorityStr = wo.getPriority().name();
            priorityCounts.put(priorityStr, priorityCounts.getOrDefault(priorityStr, 0L) + 1);

            String techName = wo.getAssignedTo() != null ?
                    wo.getAssignedTo().getFirstName() + " " + wo.getAssignedTo().getLastName() : "Unassigned";
            technicianWorkload.put(techName, technicianWorkload.getOrDefault(techName, 0L) + 1);

            String siteName = wo.getSite() != null ? wo.getSite().getName() : "Unknown Site";
            siteBreakdown.put(siteName, siteBreakdown.getOrDefault(siteName, 0L) + 1);

            boolean isTerminal = wo.getStatus() == WorkOrderStatus.CLOSED || wo.getStatus() == WorkOrderStatus.CANCELLED || wo.getStatus() == WorkOrderStatus.COMPLETED;
            if (!isTerminal) {
                activeCount++;
                if (wo.getSlaDueDate() != null && now.isAfter(wo.getSlaDueDate())) {
                    overdueCount++;
                }
            }

            if (wo.getSlaBreached() != null && wo.getSlaBreached()) {
                slaBreachedCount++;
            }

            if (wo.getStatus() == WorkOrderStatus.COMPLETED || wo.getStatus() == WorkOrderStatus.CLOSED) {
                totalFinished++;
                if (wo.getSlaBreached() == null || !wo.getSlaBreached()) {
                    slaMetFinished++;
                }
            }
        }

        double complianceRate = totalFinished > 0 ? ((double) slaMetFinished / totalFinished) * 100.0 : 100.0;

        return DashboardReportDTO.builder()
                .totalWorkOrders(totalWorkOrders)
                .activeWorkOrders(activeCount)
                .overdueWorkOrders(overdueCount)
                .slaBreachedCount(slaBreachedCount)
                .slaCompliancePercentage(Math.round(complianceRate * 10.0) / 10.0)
                .statusCounts(statusCounts)
                .priorityCounts(priorityCounts)
                .technicianWorkload(technicianWorkload)
                .siteBreakdown(siteBreakdown)
                .build();
    }
}
