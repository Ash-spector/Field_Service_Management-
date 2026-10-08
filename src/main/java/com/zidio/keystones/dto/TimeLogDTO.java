package com.zidio.keystones.dto;

import java.time.LocalDateTime;

public class TimeLogDTO {
    private Long id;
    private Long workOrderId;
    private Long technicianId;
    private String technicianName;
    private Integer minutes;
    private String note;
    private LocalDateTime loggedAt;

    public TimeLogDTO() {}

    public TimeLogDTO(Long id, Long workOrderId, Long technicianId, String technicianName, Integer minutes, String note, LocalDateTime loggedAt) {
        this.id = id;
        this.workOrderId = workOrderId;
        this.technicianId = technicianId;
        this.technicianName = technicianName;
        this.minutes = minutes;
        this.note = note;
        this.loggedAt = loggedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getWorkOrderId() { return workOrderId; }
    public void setWorkOrderId(Long workOrderId) { this.workOrderId = workOrderId; }

    public Long getTechnicianId() { return technicianId; }
    public void setTechnicianId(Long technicianId) { this.technicianId = technicianId; }

    public String getTechnicianName() { return technicianName; }
    public void setTechnicianName(String technicianName) { this.technicianName = technicianName; }

    public Integer getMinutes() { return minutes; }
    public void setMinutes(Integer minutes) { this.minutes = minutes; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public LocalDateTime getLoggedAt() { return loggedAt; }
    public void setLoggedAt(LocalDateTime loggedAt) { this.loggedAt = loggedAt; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private Long workOrderId;
        private Long technicianId;
        private String technicianName;
        private Integer minutes;
        private String note;
        private LocalDateTime loggedAt;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder workOrderId(Long workOrderId) { this.workOrderId = workOrderId; return this; }
        public Builder technicianId(Long technicianId) { this.technicianId = technicianId; return this; }
        public Builder technicianName(String technicianName) { this.technicianName = technicianName; return this; }
        public Builder minutes(Integer minutes) { this.minutes = minutes; return this; }
        public Builder note(String note) { this.note = note; return this; }
        public Builder loggedAt(LocalDateTime loggedAt) { this.loggedAt = loggedAt; return this; }

        public TimeLogDTO build() {
            return new TimeLogDTO(id, workOrderId, technicianId, technicianName, minutes, note, loggedAt);
        }
    }
}
