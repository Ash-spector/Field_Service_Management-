package com.zidio.keystones.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class CreatePartDTO {
    @NotBlank(message = "Part name is required")
    private String name;

    @NotBlank(message = "SKU is required")
    private String sku;

    @NotNull(message = "Unit cost is required")
    @Min(value = 0, message = "Unit cost must be positive")
    private BigDecimal unitCost;

    @NotNull(message = "Stock quantity is required")
    @Min(value = 0, message = "Stock quantity cannot be negative")
    private Integer stockQty;

    public CreatePartDTO() {}

    public CreatePartDTO(String name, String sku, BigDecimal unitCost, Integer stockQty) {
        this.name = name;
        this.sku = sku;
        this.unitCost = unitCost;
        this.stockQty = stockQty;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public BigDecimal getUnitCost() { return unitCost; }
    public void setUnitCost(BigDecimal unitCost) { this.unitCost = unitCost; }

    public Integer getStockQty() { return stockQty; }
    public void setStockQty(Integer stockQty) { this.stockQty = stockQty; }
}
