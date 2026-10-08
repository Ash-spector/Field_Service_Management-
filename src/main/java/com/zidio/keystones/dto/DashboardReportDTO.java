package com.zidio.keystones.dto;

import java.util.Map;

public class DashboardReportDTO {
    private long totalWorkOrders;
    private long activeWorkOrders;
    private long overdueWorkOrders;
    private long slaBreachedCount;
    private double slaCompliancePercentage;

    private Map<String, Long> statusCounts;
    private Map<String, Long> priorityCounts;
    private Map<String, Long> technicianWorkload;
    private Map<String, Long> siteBreakdown;

    public DashboardReportDTO() {}

    public DashboardReportDTO(long totalWorkOrders, long activeWorkOrders, long overdueWorkOrders, long slaBreachedCount, double slaCompliancePercentage, Map<String, Long> statusCounts, Map<String, Long> priorityCounts, Map<String, Long> technicianWorkload, Map<String, Long> siteBreakdown) {
        this.totalWorkOrders = totalWorkOrders;
        this.activeWorkOrders = activeWorkOrders;
        this.overdueWorkOrders = overdueWorkOrders;
        this.slaBreachedCount = slaBreachedCount;
        this.slaCompliancePercentage = slaCompliancePercentage;
        this.statusCounts = statusCounts;
        this.priorityCounts = priorityCounts;
        this.technicianWorkload = technicianWorkload;
        this.siteBreakdown = siteBreakdown;
    }

    public long getTotalWorkOrders() { return totalWorkOrders; }
    public void setTotalWorkOrders(long totalWorkOrders) { this.totalWorkOrders = totalWorkOrders; }

    public long getActiveWorkOrders() { return activeWorkOrders; }
    public void setActiveWorkOrders(long activeWorkOrders) { this.activeWorkOrders = activeWorkOrders; }

    public long getOverdueWorkOrders() { return overdueWorkOrders; }
    public void setOverdueWorkOrders(long overdueWorkOrders) { this.overdueWorkOrders = overdueWorkOrders; }

    public long getSlaBreachedCount() { return slaBreachedCount; }
    public void setSlaBreachedCount(long slaBreachedCount) { this.slaBreachedCount = slaBreachedCount; }

    public double getSlaCompliancePercentage() { return slaCompliancePercentage; }
    public void setSlaCompliancePercentage(double slaCompliancePercentage) { this.slaCompliancePercentage = slaCompliancePercentage; }

    public Map<String, Long> getStatusCounts() { return statusCounts; }
    public void setStatusCounts(Map<String, Long> statusCounts) { this.statusCounts = statusCounts; }

    public Map<String, Long> getPriorityCounts() { return priorityCounts; }
    public void setPriorityCounts(Map<String, Long> priorityCounts) { this.priorityCounts = priorityCounts; }

    public Map<String, Long> getTechnicianWorkload() { return technicianWorkload; }
    public void setTechnicianWorkload(Map<String, Long> technicianWorkload) { this.technicianWorkload = technicianWorkload; }

    public Map<String, Long> getSiteBreakdown() { return siteBreakdown; }
    public void setSiteBreakdown(Map<String, Long> siteBreakdown) { this.siteBreakdown = siteBreakdown; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private long totalWorkOrders;
        private long activeWorkOrders;
        private long overdueWorkOrders;
        private long slaBreachedCount;
        private double slaCompliancePercentage;
        private Map<String, Long> statusCounts;
        private Map<String, Long> priorityCounts;
        private Map<String, Long> technicianWorkload;
        private Map<String, Long> siteBreakdown;

        public Builder totalWorkOrders(long totalWorkOrders) { this.totalWorkOrders = totalWorkOrders; return this; }
        public Builder activeWorkOrders(long activeWorkOrders) { this.activeWorkOrders = activeWorkOrders; return this; }
        public Builder overdueWorkOrders(long overdueWorkOrders) { this.overdueWorkOrders = overdueWorkOrders; return this; }
        public Builder slaBreachedCount(long slaBreachedCount) { this.slaBreachedCount = slaBreachedCount; return this; }
        public Builder slaCompliancePercentage(double slaCompliancePercentage) { this.slaCompliancePercentage = slaCompliancePercentage; return this; }
        public Builder statusCounts(Map<String, Long> statusCounts) { this.statusCounts = statusCounts; return this; }
        public Builder priorityCounts(Map<String, Long> priorityCounts) { this.priorityCounts = priorityCounts; return this; }
        public Builder technicianWorkload(Map<String, Long> technicianWorkload) { this.technicianWorkload = technicianWorkload; return this; }
        public Builder siteBreakdown(Map<String, Long> siteBreakdown) { this.siteBreakdown = siteBreakdown; return this; }

        public DashboardReportDTO build() {
            return new DashboardReportDTO(totalWorkOrders, activeWorkOrders, overdueWorkOrders, slaBreachedCount, slaCompliancePercentage, statusCounts, priorityCounts, technicianWorkload, siteBreakdown);
        }
    }
}
