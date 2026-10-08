package com.zidio.keystones.dto;

import com.zidio.keystones.enums.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateWorkOrderDTO {
    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotNull(message = "Priority is required")
    private Priority priority;

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotNull(message = "Site ID is required")
    private Long siteId;

    private Long assignedToId;

    public CreateWorkOrderDTO() {}

    public CreateWorkOrderDTO(String title, String description, Priority priority, Long customerId, Long siteId, Long assignedToId) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.customerId = customerId;
        this.siteId = siteId;
        this.assignedToId = assignedToId;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Priority getPriority() { return priority; }
    public void setPriority(Priority priority) { this.priority = priority; }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public Long getSiteId() { return siteId; }
    public void setSiteId(Long siteId) { this.siteId = siteId; }

    public Long getAssignedToId() { return assignedToId; }
    public void setAssignedToId(Long assignedToId) { this.assignedToId = assignedToId; }
}
