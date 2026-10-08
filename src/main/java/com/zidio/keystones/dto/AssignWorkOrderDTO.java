package com.zidio.keystones.dto;

import jakarta.validation.constraints.NotNull;

public class AssignWorkOrderDTO {
    @NotNull(message = "Technician ID is required")
    private Long technicianId;

    private String note;

    public AssignWorkOrderDTO() {}

    public AssignWorkOrderDTO(Long technicianId, String note) {
        this.technicianId = technicianId;
        this.note = note;
    }

    public Long getTechnicianId() { return technicianId; }
    public void setTechnicianId(Long technicianId) { this.technicianId = technicianId; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
