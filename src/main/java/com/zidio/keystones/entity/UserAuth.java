package com.zidio.keystones.entity;

import com.zidio.keystones.enums.Role;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "user_auth")
public class UserAuth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;
    private String phone;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private String resetToken;

    private Date resetTokenExpiry;

    public UserAuth() {}

    public UserAuth(Long id, String firstName, String lastName, String email, String password, String phone, Role role, Customer customer, String resetToken, Date resetTokenExpiry) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.role = role;
        this.customer = customer;
        this.resetToken = resetToken;
        this.resetTokenExpiry = resetTokenExpiry;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public String getResetToken() { return resetToken; }
    public void setResetToken(String resetToken) { this.resetToken = resetToken; }

    public Date getResetTokenExpiry() { return resetTokenExpiry; }
    public void setResetTokenExpiry(Date resetTokenExpiry) { this.resetTokenExpiry = resetTokenExpiry; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String firstName;
        private String lastName;
        private String email;
        private String password;
        private String phone;
        private Role role;
        private Customer customer;
        private String resetToken;
        private Date resetTokenExpiry;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder firstName(String firstName) { this.firstName = firstName; return this; }
        public Builder lastName(String lastName) { this.lastName = lastName; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Builder password(String password) { this.password = password; return this; }
        public Builder phone(String phone) { this.phone = phone; return this; }
        public Builder role(Role role) { this.role = role; return this; }
        public Builder customer(Customer customer) { this.customer = customer; return this; }
        public Builder resetToken(String resetToken) { this.resetToken = resetToken; return this; }
        public Builder resetTokenExpiry(Date resetTokenExpiry) { this.resetTokenExpiry = resetTokenExpiry; return this; }

        public UserAuth build() {
            return new UserAuth(id, firstName, lastName, email, password, phone, role, customer, resetToken, resetTokenExpiry);
        }
    }
}
