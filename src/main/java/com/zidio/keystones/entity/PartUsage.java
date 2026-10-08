package com.zidio.keystones.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "part_usage")
public class PartUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_order_id", nullable = false)
    private WorkOrder workOrder;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "part_id", nullable = false)
    private Part part;

    @Column(name = "qty_used", nullable = false)
    private Integer qtyUsed;

    @Column(name = "unit_cost", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitCost;

    @Column(name = "used_at", nullable = false, updatable = false)
    private LocalDateTime usedAt;

    public PartUsage() {}

    public PartUsage(Long id, WorkOrder workOrder, Part part, Integer qtyUsed, BigDecimal unitCost, LocalDateTime usedAt) {
        this.id = id;
        this.workOrder = workOrder;
        this.part = part;
        this.qtyUsed = qtyUsed;
        this.unitCost = unitCost;
        this.usedAt = usedAt;
    }

    @PrePersist
    protected void onCreate() {
        if (usedAt == null) usedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public WorkOrder getWorkOrder() { return workOrder; }
    public void setWorkOrder(WorkOrder workOrder) { this.workOrder = workOrder; }

    public Part getPart() { return part; }
    public void setPart(Part part) { this.part = part; }

    public Integer getQtyUsed() { return qtyUsed; }
    public void setQtyUsed(Integer qtyUsed) { this.qtyUsed = qtyUsed; }

    public BigDecimal getUnitCost() { return unitCost; }
    public void setUnitCost(BigDecimal unitCost) { this.unitCost = unitCost; }

    public LocalDateTime getUsedAt() { return usedAt; }
    public void setUsedAt(LocalDateTime usedAt) { this.usedAt = usedAt; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private WorkOrder workOrder;
        private Part part;
        private Integer qtyUsed;
        private BigDecimal unitCost;
        private LocalDateTime usedAt;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder workOrder(WorkOrder workOrder) { this.workOrder = workOrder; return this; }
        public Builder part(Part part) { this.part = part; return this; }
        public Builder qtyUsed(Integer qtyUsed) { this.qtyUsed = qtyUsed; return this; }
        public Builder unitCost(BigDecimal unitCost) { this.unitCost = unitCost; return this; }
        public Builder usedAt(LocalDateTime usedAt) { this.usedAt = usedAt; return this; }

        public PartUsage build() {
            return new PartUsage(id, workOrder, part, qtyUsed, unitCost, usedAt);
        }
    }
}
