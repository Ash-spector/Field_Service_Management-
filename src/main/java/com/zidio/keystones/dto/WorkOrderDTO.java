package com.zidio.keystones.dto;

import com.zidio.keystones.enums.Priority;
import com.zidio.keystones.enums.SlaStatus;
import com.zidio.keystones.enums.WorkOrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class WorkOrderDTO {
    private Long id;
    private String code;
    private String title;
    private String description;
    private Priority priority;
    private WorkOrderStatus status;
    private Long customerId;
    private String customerName;
    private Long siteId;
    private String siteName;
    private Long assignedToId;
    private String assignedToName;
    private LocalDateTime slaDueDate;
    private Boolean slaBreached;
    private SlaStatus slaStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private BigDecimal totalPartsCost;
    private Integer totalTimeMinutes;

    private List<WorkOrderStatusHistoryDTO> history;
    private List<PartUsageDTO> partsUsed;
    private List<TimeLogDTO> timeLogs;

    public WorkOrderDTO() {}

    public WorkOrderDTO(Long id, String code, String title, String description, Priority priority, WorkOrderStatus status, Long customerId, String customerName, Long siteId, String siteName, Long assignedToId, String assignedToName, LocalDateTime slaDueDate, Boolean slaBreached, SlaStatus slaStatus, LocalDateTime createdAt, LocalDateTime updatedAt, BigDecimal totalPartsCost, Integer totalTimeMinutes, List<WorkOrderStatusHistoryDTO> history, List<PartUsageDTO> partsUsed, List<TimeLogDTO> timeLogs) {
        this.id = id;
        this.code = code;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.status = status;
        this.customerId = customerId;
        this.customerName = customerName;
        this.siteId = siteId;
        this.siteName = siteName;
        this.assignedToId = assignedToId;
        this.assignedToName = assignedToName;
        this.slaDueDate = slaDueDate;
        this.slaBreached = slaBreached;
        this.slaStatus = slaStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.totalPartsCost = totalPartsCost;
        this.totalTimeMinutes = totalTimeMinutes;
        this.history = history;
        this.partsUsed = partsUsed;
        this.timeLogs = timeLogs;
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

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public Long getSiteId() { return siteId; }
    public void setSiteId(Long siteId) { this.siteId = siteId; }

    public String getSiteName() { return siteName; }
    public void setSiteName(String siteName) { this.siteName = siteName; }

    public Long getAssignedToId() { return assignedToId; }
    public void setAssignedToId(Long assignedToId) { this.assignedToId = assignedToId; }

    public String getAssignedToName() { return assignedToName; }
    public void setAssignedToName(String assignedToName) { this.assignedToName = assignedToName; }

    public LocalDateTime getSlaDueDate() { return slaDueDate; }
    public void setSlaDueDate(LocalDateTime slaDueDate) { this.slaDueDate = slaDueDate; }

    public Boolean getSlaBreached() { return slaBreached; }
    public void setSlaBreached(Boolean slaBreached) { this.slaBreached = slaBreached; }

    public SlaStatus getSlaStatus() { return slaStatus; }
    public void setSlaStatus(SlaStatus slaStatus) { this.slaStatus = slaStatus; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public BigDecimal getTotalPartsCost() { return totalPartsCost; }
    public void setTotalPartsCost(BigDecimal totalPartsCost) { this.totalPartsCost = totalPartsCost; }

    public Integer getTotalTimeMinutes() { return totalTimeMinutes; }
    public void setTotalTimeMinutes(Integer totalTimeMinutes) { this.totalTimeMinutes = totalTimeMinutes; }

    public List<WorkOrderStatusHistoryDTO> getHistory() { return history; }
    public void setHistory(List<WorkOrderStatusHistoryDTO> history) { this.history = history; }

    public List<PartUsageDTO> getPartsUsed() { return partsUsed; }
    public void setPartsUsed(List<PartUsageDTO> partsUsed) { this.partsUsed = partsUsed; }

    public List<TimeLogDTO> getTimeLogs() { return timeLogs; }
    public void setTimeLogs(List<TimeLogDTO> timeLogs) { this.timeLogs = timeLogs; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String code;
        private String title;
        private String description;
        private Priority priority;
        private WorkOrderStatus status;
        private Long customerId;
        private String customerName;
        private Long siteId;
        private String siteName;
        private Long assignedToId;
        private String assignedToName;
        private LocalDateTime slaDueDate;
        private Boolean slaBreached;
        private SlaStatus slaStatus;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private BigDecimal totalPartsCost;
        private Integer totalTimeMinutes;
        private List<WorkOrderStatusHistoryDTO> history;
        private List<PartUsageDTO> partsUsed;
        private List<TimeLogDTO> timeLogs;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder code(String code) { this.code = code; return this; }
        public Builder title(String title) { this.title = title; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder priority(Priority priority) { this.priority = priority; return this; }
        public Builder status(WorkOrderStatus status) { this.status = status; return this; }
        public Builder customerId(Long customerId) { this.customerId = customerId; return this; }
        public Builder customerName(String customerName) { this.customerName = customerName; return this; }
        public Builder siteId(Long siteId) { this.siteId = siteId; return this; }
        public Builder siteName(String siteName) { this.siteName = siteName; return this; }
        public Builder assignedToId(Long assignedToId) { this.assignedToId = assignedToId; return this; }
        public Builder assignedToName(String assignedToName) { this.assignedToName = assignedToName; return this; }
        public Builder slaDueDate(LocalDateTime slaDueDate) { this.slaDueDate = slaDueDate; return this; }
        public Builder slaBreached(Boolean slaBreached) { this.slaBreached = slaBreached; return this; }
        public Builder slaStatus(SlaStatus slaStatus) { this.slaStatus = slaStatus; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }
        public Builder totalPartsCost(BigDecimal totalPartsCost) { this.totalPartsCost = totalPartsCost; return this; }
        public Builder totalTimeMinutes(Integer totalTimeMinutes) { this.totalTimeMinutes = totalTimeMinutes; return this; }
        public Builder history(List<WorkOrderStatusHistoryDTO> history) { this.history = history; return this; }
        public Builder partsUsed(List<PartUsageDTO> partsUsed) { this.partsUsed = partsUsed; return this; }
        public Builder timeLogs(List<TimeLogDTO> timeLogs) { this.timeLogs = timeLogs; return this; }

        public WorkOrderDTO build() {
            return new WorkOrderDTO(id, code, title, description, priority, status, customerId, customerName, siteId, siteName, assignedToId, assignedToName, slaDueDate, slaBreached, slaStatus, createdAt, updatedAt, totalPartsCost, totalTimeMinutes, history, partsUsed, timeLogs);
        }
    }
}
