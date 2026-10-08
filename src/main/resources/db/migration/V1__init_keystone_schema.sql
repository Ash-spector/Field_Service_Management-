-- V1__init_keystone_schema.sql
-- Table: customer
CREATE TABLE IF NOT EXISTS customer (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    contact_email VARCHAR(255),
    phone VARCHAR(50),
    address VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Table: site
CREATE TABLE IF NOT EXISTS site (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(500),
    contact_name VARCHAR(255),
    contact_phone VARCHAR(50),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_site_customer FOREIGN KEY (customer_id) REFERENCES customer(id) ON DELETE CASCADE
);

-- Table: user_auth
CREATE TABLE IF NOT EXISTS user_auth (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255),
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(50),
    role VARCHAR(50) NOT NULL,
    customer_id BIGINT,
    reset_token VARCHAR(255),
    reset_token_expiry TIMESTAMP,
    CONSTRAINT fk_user_customer FOREIGN KEY (customer_id) REFERENCES customer(id) ON DELETE SET NULL
);

-- Table: work_order
CREATE TABLE IF NOT EXISTS work_order (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(2000),
    priority VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    customer_id BIGINT NOT NULL,
    site_id BIGINT NOT NULL,
    assigned_to_id BIGINT,
    sla_due_date TIMESTAMP,
    sla_breached BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_wo_customer FOREIGN KEY (customer_id) REFERENCES customer(id),
    CONSTRAINT fk_wo_site FOREIGN KEY (site_id) REFERENCES site(id),
    CONSTRAINT fk_wo_assigned FOREIGN KEY (assigned_to_id) REFERENCES user_auth(id)
);

-- Table: work_order_status_history
CREATE TABLE IF NOT EXISTS work_order_status_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    work_order_id BIGINT NOT NULL,
    from_status VARCHAR(20),
    to_status VARCHAR(20) NOT NULL,
    changed_by VARCHAR(255) NOT NULL,
    changed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    note VARCHAR(1000),
    CONSTRAINT fk_wosh_wo FOREIGN KEY (work_order_id) REFERENCES work_order(id) ON DELETE CASCADE
);

-- Table: part
CREATE TABLE IF NOT EXISTS part (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    sku VARCHAR(100) NOT NULL UNIQUE,
    unit_cost DECIMAL(10, 2) NOT NULL,
    stock_qty INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Table: part_usage
CREATE TABLE IF NOT EXISTS part_usage (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    work_order_id BIGINT NOT NULL,
    part_id BIGINT NOT NULL,
    qty_used INT NOT NULL,
    unit_cost DECIMAL(10, 2) NOT NULL,
    used_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_pu_wo FOREIGN KEY (work_order_id) REFERENCES work_order(id) ON DELETE CASCADE,
    CONSTRAINT fk_pu_part FOREIGN KEY (part_id) REFERENCES part(id)
);

-- Table: time_log
CREATE TABLE IF NOT EXISTS time_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    work_order_id BIGINT NOT NULL,
    technician_id BIGINT NOT NULL,
    minutes INT NOT NULL,
    note VARCHAR(1000),
    logged_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_tl_wo FOREIGN KEY (work_order_id) REFERENCES work_order(id) ON DELETE CASCADE,
    CONSTRAINT fk_tl_tech FOREIGN KEY (technician_id) REFERENCES user_auth(id)
);
