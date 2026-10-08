package com.zidio.keystones.entity;

import com.zidio.keystones.enums.Priority;
import com.zidio.keystones.enums.WorkOrderStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "work_order")
public class WorkOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WorkOrderStatus status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "site_id", nullable = false)
    private Site site;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "assigned_to_id")
    private UserAuth assignedTo;

    @Column(name = "sla_due_date")
    private LocalDateTime slaDueDate;

    @Column(name = "sla_breached")
    private Boolean slaBreached = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public WorkOrder() {}

    public WorkOrder(Long id, String code, String title, String description, Priority priority, WorkOrderStatus status, Customer customer, Site site, UserAuth assignedTo, LocalDateTime slaDueDate, Boolean slaBreached, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.code = code;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.status = status;
        this.customer = customer;
        this.site = site;
        this.assignedTo = assignedTo;
        this.slaDueDate = slaDueDate;
        this.slaBreached = slaBreached;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (updatedAt == null) updatedAt = LocalDateTime.now();
        if (slaBreached == null) slaBreached = false;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Priority getPriority() { return priority; }
    public void setPriority(Priority priority) { this.priority = priority; }

    public WorkOrderStatus getStatus() { return status; }
    public void setStatus(WorkOrderStatus status) { this.status = status; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public Site getSite() { return site; }
    public void setSite(Site site) { this.site = site; }

    public UserAuth getAssignedTo() { return assignedTo; }
    public void setAssignedTo(UserAuth assignedTo) { this.assignedTo = assignedTo; }

    public LocalDateTime getSlaDueDate() { return slaDueDate; }
    public void setSlaDueDate(LocalDateTime slaDueDate) { this.slaDueDate = slaDueDate; }

    public Boolean getSlaBreached() { return slaBreached; }
    public void setSlaBreached(Boolean slaBreached) { this.slaBreached = slaBreached; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String code;
        private String title;
        private String description;
        private Priority priority;
        private WorkOrderStatus status;
        private Customer customer;
        private Site site;
        private UserAuth assignedTo;
        private LocalDateTime slaDueDate;
        private Boolean slaBreached = false;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder code(String code) { this.code = code; return this; }
        public Builder title(String title) { this.title = title; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder priority(Priority priority) { this.priority = priority; return this; }
        public Builder status(WorkOrderStatus status) { this.status = status; return this; }
        public Builder customer(Customer customer) { this.customer = customer; return this; }
        public Builder site(Site site) { this.site = site; return this; }
        public Builder assignedTo(UserAuth assignedTo) { this.assignedTo = assignedTo; return this; }
        public Builder slaDueDate(LocalDateTime slaDueDate) { this.slaDueDate = slaDueDate; return this; }
        public Builder slaBreached(Boolean slaBreached) { this.slaBreached = slaBreached; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public WorkOrder build() {
            return new WorkOrder(id, code, title, description, priority, status, customer, site, assignedTo, slaDueDate, slaBreached, createdAt, updatedAt);
        }
    }
}
