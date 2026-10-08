package com.zidio.keystones.entity;

import com.zidio.keystones.enums.WorkOrderStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "work_order_status_history")
public class WorkOrderStatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_order_id", nullable = false)
    private WorkOrder workOrder;

    @Enumerated(EnumType.STRING)
    @Column(name = "from_status")
    private WorkOrderStatus fromStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "to_status", nullable = false)
    private WorkOrderStatus toStatus;

    @Column(name = "changed_by", nullable = false)
    private String changedBy;

    @Column(name = "changed_at", nullable = false, updatable = false)
    private LocalDateTime changedAt;

    @Column(length = 1000)
    private String note;

    public WorkOrderStatusHistory() {}

    public WorkOrderStatusHistory(Long id, WorkOrder workOrder, WorkOrderStatus fromStatus, WorkOrderStatus toStatus, String changedBy, LocalDateTime changedAt, String note) {
        this.id = id;
        this.workOrder = workOrder;
        this.fromStatus = fromStatus;
        this.toStatus = toStatus;
        this.changedBy = changedBy;
        this.changedAt = changedAt;
        this.note = note;
    }

    @PrePersist
    protected void onCreate() {
        if (changedAt == null) changedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public WorkOrder getWorkOrder() { return workOrder; }
    public void setWorkOrder(WorkOrder workOrder) { this.workOrder = workOrder; }

    public WorkOrderStatus getFromStatus() { return fromStatus; }
    public void setFromStatus(WorkOrderStatus fromStatus) { this.fromStatus = fromStatus; }

    public WorkOrderStatus getToStatus() { return toStatus; }
    public void setToStatus(WorkOrderStatus toStatus) { this.toStatus = toStatus; }

    public String getChangedBy() { return changedBy; }
    public void setChangedBy(String changedBy) { this.changedBy = changedBy; }

    public LocalDateTime getChangedAt() { return changedAt; }
    public void setChangedAt(LocalDateTime changedAt) { this.changedAt = changedAt; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private WorkOrder workOrder;
        private WorkOrderStatus fromStatus;
        private WorkOrderStatus toStatus;
        private String changedBy;
        private LocalDateTime changedAt;
        private String note;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder workOrder(WorkOrder workOrder) { this.workOrder = workOrder; return this; }
        public Builder fromStatus(WorkOrderStatus fromStatus) { this.fromStatus = fromStatus; return this; }
        public Builder toStatus(WorkOrderStatus toStatus) { this.toStatus = toStatus; return this; }
        public Builder changedBy(String changedBy) { this.changedBy = changedBy; return this; }
        public Builder changedAt(LocalDateTime changedAt) { this.changedAt = changedAt; return this; }
        public Builder note(String note) { this.note = note; return this; }

        public WorkOrderStatusHistory build() {
            return new WorkOrderStatusHistory(id, workOrder, fromStatus, toStatus, changedBy, changedAt, note);
        }
    }
}
