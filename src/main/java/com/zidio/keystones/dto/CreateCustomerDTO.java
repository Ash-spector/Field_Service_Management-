package com.zidio.keystones.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateCustomerDTO {
    @NotBlank(message = "Customer name is required")
    private String name;

    private String contactEmail;
    private String phone;
    private String address;

    public CreateCustomerDTO() {}

    public CreateCustomerDTO(String name, String contactEmail, String phone, String address) {
        this.name = name;
        this.contactEmail = contactEmail;
        this.phone = phone;
        this.address = address;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}
