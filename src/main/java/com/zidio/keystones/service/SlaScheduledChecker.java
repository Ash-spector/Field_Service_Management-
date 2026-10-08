package com.zidio.keystones.service;

import com.zidio.keystones.entity.WorkOrder;
import com.zidio.keystones.repository.WorkOrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SlaScheduledChecker {

    private static final Logger log = LoggerFactory.getLogger(SlaScheduledChecker.class);

    private final WorkOrderRepository workOrderRepository;

    public SlaScheduledChecker(WorkOrderRepository workOrderRepository) {
        this.workOrderRepository = workOrderRepository;
    }

    @Scheduled(cron = "0 */1 * * * *") // Check every 1 minute
    @Transactional
    public void checkSlaBreaches() {
        LocalDateTime now = LocalDateTime.now();
        List<WorkOrder> overdueOrders = workOrderRepository.findOverdueWorkOrders(now);

        for (WorkOrder order : overdueOrders) {
            if (order.getSlaBreached() == null || !order.getSlaBreached()) {
                order.setSlaBreached(true);
                workOrderRepository.save(order);
                log.warn("SLA BREACH ALERT: Work Order [{}] ({}) has breached its SLA due date [{}]",
                        order.getCode(), order.getTitle(), order.getSlaDueDate());
            }
        }
    }
}
