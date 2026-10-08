package com.zidio.keystones.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PartDTO {
    private Long id;
    private String name;
    private String sku;
    private BigDecimal unitCost;
    private Integer stockQty;
    private LocalDateTime createdAt;

    public PartDTO() {}

    public PartDTO(Long id, String name, String sku, BigDecimal unitCost, Integer stockQty, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.sku = sku;
        this.unitCost = unitCost;
        this.stockQty = stockQty;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public BigDecimal getUnitCost() { return unitCost; }
    public void setUnitCost(BigDecimal unitCost) { this.unitCost = unitCost; }

    public Integer getStockQty() { return stockQty; }
    public void setStockQty(Integer stockQty) { this.stockQty = stockQty; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String name;
        private String sku;
        private BigDecimal unitCost;
        private Integer stockQty;
        private LocalDateTime createdAt;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder sku(String sku) { this.sku = sku; return this; }
        public Builder unitCost(BigDecimal unitCost) { this.unitCost = unitCost; return this; }
        public Builder stockQty(Integer stockQty) { this.stockQty = stockQty; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public PartDTO build() {
            return new PartDTO(id, name, sku, unitCost, stockQty, createdAt);
        }
    }
}
