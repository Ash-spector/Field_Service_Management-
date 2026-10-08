package com.zidio.keystones.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateSiteDTO {
    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotBlank(message = "Site name is required")
    private String name;

    private String address;
    private String contactName;
    private String contactPhone;

    public CreateSiteDTO() {}

    public CreateSiteDTO(Long customerId, String name, String address, String contactName, String contactPhone) {
        this.customerId = customerId;
        this.name = name;
        this.address = address;
        this.contactName = contactName;
        this.contactPhone = contactPhone;
    }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getContactName() { return contactName; }
    public void setContactName(String contactName) { this.contactName = contactName; }

    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }
}
