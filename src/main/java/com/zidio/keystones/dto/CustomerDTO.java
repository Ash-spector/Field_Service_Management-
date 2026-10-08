package com.zidio.keystones.dto;

import java.time.LocalDateTime;

public class CustomerDTO {
    private Long id;
    private String name;
    private String contactEmail;
    private String phone;
    private String address;
    private LocalDateTime createdAt;
    private int siteCount;

    public CustomerDTO() {}

    public CustomerDTO(Long id, String name, String contactEmail, String phone, String address, LocalDateTime createdAt, int siteCount) {
        this.id = id;
        this.name = name;
        this.contactEmail = contactEmail;
        this.phone = phone;
        this.address = address;
        this.createdAt = createdAt;
        this.siteCount = siteCount;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public int getSiteCount() { return siteCount; }
    public void setSiteCount(int siteCount) { this.siteCount = siteCount; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String name;
        private String contactEmail;
        private String phone;
        private String address;
        private LocalDateTime createdAt;
        private int siteCount;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder contactEmail(String contactEmail) { this.contactEmail = contactEmail; return this; }
        public Builder phone(String phone) { this.phone = phone; return this; }
        public Builder address(String address) { this.address = address; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder siteCount(int siteCount) { this.siteCount = siteCount; return this; }

        public CustomerDTO build() {
            return new CustomerDTO(id, name, contactEmail, phone, address, createdAt, siteCount);
        }
    }
}
