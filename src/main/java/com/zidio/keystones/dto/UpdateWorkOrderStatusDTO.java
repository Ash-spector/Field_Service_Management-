package com.zidio.keystones.dto;

import com.zidio.keystones.enums.WorkOrderStatus;
import jakarta.validation.constraints.NotNull;

public class UpdateWorkOrderStatusDTO {
    @NotNull(message = "Target status is required")
    private WorkOrderStatus status;

    private String note;

    public UpdateWorkOrderStatusDTO() {}

    public UpdateWorkOrderStatusDTO(WorkOrderStatus status, String note) {
        this.status = status;
        this.note = note;
    }

    public WorkOrderStatus getStatus() { return status; }
    public void setStatus(WorkOrderStatus status) { this.status = status; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
