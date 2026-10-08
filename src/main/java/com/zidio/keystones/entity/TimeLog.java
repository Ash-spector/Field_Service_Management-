package com.zidio.keystones.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "time_log")
public class TimeLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_order_id", nullable = false)
    private WorkOrder workOrder;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "technician_id", nullable = false)
    private UserAuth technician;

    @Column(nullable = false)
    private Integer minutes;

    @Column(length = 1000)
    private String note;

    @Column(name = "logged_at", nullable = false, updatable = false)
    private LocalDateTime loggedAt;

    public TimeLog() {}

    public TimeLog(Long id, WorkOrder workOrder, UserAuth technician, Integer minutes, String note, LocalDateTime loggedAt) {
        this.id = id;
        this.workOrder = workOrder;
        this.technician = technician;
        this.minutes = minutes;
        this.note = note;
        this.loggedAt = loggedAt;
    }

    @PrePersist
    protected void onCreate() {
        if (loggedAt == null) loggedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public WorkOrder getWorkOrder() { return workOrder; }
    public void setWorkOrder(WorkOrder workOrder) { this.workOrder = workOrder; }

    public UserAuth getTechnician() { return technician; }
    public void setTechnician(UserAuth technician) { this.technician = technician; }

    public Integer getMinutes() { return minutes; }
    public void setMinutes(Integer minutes) { this.minutes = minutes; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public LocalDateTime getLoggedAt() { return loggedAt; }
    public void setLoggedAt(LocalDateTime loggedAt) { this.loggedAt = loggedAt; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private WorkOrder workOrder;
        private UserAuth technician;
        private Integer minutes;
        private String note;
        private LocalDateTime loggedAt;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder workOrder(WorkOrder workOrder) { this.workOrder = workOrder; return this; }
        public Builder technician(UserAuth technician) { this.technician = technician; return this; }
        public Builder minutes(Integer minutes) { this.minutes = minutes; return this; }
        public Builder note(String note) { this.note = note; return this; }
        public Builder loggedAt(LocalDateTime loggedAt) { this.loggedAt = loggedAt; return this; }

        public TimeLog build() {
            return new TimeLog(id, workOrder, technician, minutes, note, loggedAt);
        }
    }
}
