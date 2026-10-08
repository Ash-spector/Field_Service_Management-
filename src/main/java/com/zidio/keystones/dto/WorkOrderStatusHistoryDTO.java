package com.zidio.keystones.dto;

import com.zidio.keystones.enums.WorkOrderStatus;
import java.time.LocalDateTime;

public class WorkOrderStatusHistoryDTO {
    private Long id;
    private Long workOrderId;
    private WorkOrderStatus fromStatus;
    private WorkOrderStatus toStatus;
    private String changedBy;
    private LocalDateTime changedAt;
    private String note;

    public WorkOrderStatusHistoryDTO() {}

    public WorkOrderStatusHistoryDTO(Long id, Long workOrderId, WorkOrderStatus fromStatus, WorkOrderStatus toStatus, String changedBy, LocalDateTime changedAt, String note) {
        this.id = id;
        this.workOrderId = workOrderId;
        this.fromStatus = fromStatus;
        this.toStatus = toStatus;
        this.changedBy = changedBy;
        this.changedAt = changedAt;
        this.note = note;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getWorkOrderId() { return workOrderId; }
    public void setWorkOrderId(Long workOrderId) { this.workOrderId = workOrderId; }

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
        private Long workOrderId;
        private WorkOrderStatus fromStatus;
        private WorkOrderStatus toStatus;
        private String changedBy;
        private LocalDateTime changedAt;
        private String note;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder workOrderId(Long workOrderId) { this.workOrderId = workOrderId; return this; }
        public Builder fromStatus(WorkOrderStatus fromStatus) { this.fromStatus = fromStatus; return this; }
        public Builder toStatus(WorkOrderStatus toStatus) { this.toStatus = toStatus; return this; }
        public Builder changedBy(String changedBy) { this.changedBy = changedBy; return this; }
        public Builder changedAt(LocalDateTime changedAt) { this.changedAt = changedAt; return this; }
        public Builder note(String note) { this.note = note; return this; }

        public WorkOrderStatusHistoryDTO build() {
            return new WorkOrderStatusHistoryDTO(id, workOrderId, fromStatus, toStatus, changedBy, changedAt, note);
        }
    }
}
