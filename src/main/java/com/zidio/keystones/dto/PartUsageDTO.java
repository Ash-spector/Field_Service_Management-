package com.zidio.keystones.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PartUsageDTO {
    private Long id;
    private Long workOrderId;
    private Long partId;
    private String partName;
    private String sku;
    private Integer qtyUsed;
    private BigDecimal unitCost;
    private BigDecimal totalCost;
    private LocalDateTime usedAt;

    public PartUsageDTO() {}

    public PartUsageDTO(Long id, Long workOrderId, Long partId, String partName, String sku, Integer qtyUsed, BigDecimal unitCost, BigDecimal totalCost, LocalDateTime usedAt) {
        this.id = id;
        this.workOrderId = workOrderId;
        this.partId = partId;
        this.partName = partName;
        this.sku = sku;
        this.qtyUsed = qtyUsed;
        this.unitCost = unitCost;
        this.totalCost = totalCost;
        this.usedAt = usedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getWorkOrderId() { return workOrderId; }
    public void setWorkOrderId(Long workOrderId) { this.workOrderId = workOrderId; }

    public Long getPartId() { return partId; }
    public void setPartId(Long partId) { this.partId = partId; }

    public String getPartName() { return partName; }
    public void setPartName(String partName) { this.partName = partName; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public Integer getQtyUsed() { return qtyUsed; }
    public void setQtyUsed(Integer qtyUsed) { this.qtyUsed = qtyUsed; }

    public BigDecimal getUnitCost() { return unitCost; }
    public void setUnitCost(BigDecimal unitCost) { this.unitCost = unitCost; }

    public BigDecimal getTotalCost() { return totalCost; }
    public void setTotalCost(BigDecimal totalCost) { this.totalCost = totalCost; }

    public LocalDateTime getUsedAt() { return usedAt; }
    public void setUsedAt(LocalDateTime usedAt) { this.usedAt = usedAt; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private Long workOrderId;
        private Long partId;
        private String partName;
        private String sku;
        private Integer qtyUsed;
        private BigDecimal unitCost;
        private BigDecimal totalCost;
        private LocalDateTime usedAt;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder workOrderId(Long workOrderId) { this.workOrderId = workOrderId; return this; }
        public Builder partId(Long partId) { this.partId = partId; return this; }
        public Builder partName(String partName) { this.partName = partName; return this; }
        public Builder sku(String sku) { this.sku = sku; return this; }
        public Builder qtyUsed(Integer qtyUsed) { this.qtyUsed = qtyUsed; return this; }
        public Builder unitCost(BigDecimal unitCost) { this.unitCost = unitCost; return this; }
        public Builder totalCost(BigDecimal totalCost) { this.totalCost = totalCost; return this; }
        public Builder usedAt(LocalDateTime usedAt) { this.usedAt = usedAt; return this; }

        public PartUsageDTO build() {
            return new PartUsageDTO(id, workOrderId, partId, partName, sku, qtyUsed, unitCost, totalCost, usedAt);
        }
    }
}
