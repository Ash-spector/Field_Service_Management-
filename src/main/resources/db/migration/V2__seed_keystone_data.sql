-- V2__seed_keystone_data.sql

-- 1. Customers
INSERT INTO customer (id, name, contact_email, phone, address, created_at) VALUES
(1, 'Acme Commercial Properties', 'contact@acmecommercial.com', '+1 555-0192', '100 Business Center Way, Tower A', CURRENT_TIMESTAMP),
(2, 'Apex Logistics & Warehousing', 'ops@apexlogistics.com', '+1 555-0144', '500 Freight Terminal Blvd, Suite 12', CURRENT_TIMESTAMP),
(3, 'Meridian Corporate Center', 'facilities@meridian.com', '+1 555-0100', '1 Meridian Plaza, Floor 8', CURRENT_TIMESTAMP);

-- 2. Sites
INSERT INTO site (id, customer_id, name, address, contact_name, contact_phone, created_at) VALUES
(1, 1, 'Acme Tower A - Main HQ', '100 Business Center Way, Fl 1-15', 'John Acme', '+1 555-0193', CURRENT_TIMESTAMP),
(2, 1, 'Acme Tech Campus Bldg 2', '104 Business Center Way, Bldg 2', 'Sarah Jenkins', '+1 555-0194', CURRENT_TIMESTAMP),
(3, 2, 'Apex Warehouse North', '500 Freight Terminal Blvd', 'Mark Davis', '+1 555-0145', CURRENT_TIMESTAMP),
(4, 3, 'Meridian HQ Plaza', '1 Meridian Plaza', 'David Miller', '+1 555-0101', CURRENT_TIMESTAMP);

-- 3. Users (Passwords: 'password123' hashed with BCrypt)
-- Hash: $2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xD0m1bC.I8d6F./K
INSERT INTO user_auth (id, first_name, last_name, email, password, phone, role, customer_id) VALUES
(1, 'System', 'Admin', 'admin@meridian.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xD0m1bC.I8d6F./K', '+1 555-9001', 'ADMIN', NULL),
(2, 'Robert', 'Vance', 'manager@meridian.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xD0m1bC.I8d6F./K', '+1 555-9002', 'MANAGER', NULL),
(3, 'Elena', 'Rostova', 'dispatcher@meridian.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xD0m1bC.I8d6F./K', '+1 555-9003', 'DISPATCHER', NULL),
(4, 'Alex', 'Taylor', 'tech.alex@meridian.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xD0m1bC.I8d6F./K', '+1 555-9004', 'TECHNICIAN', NULL),
(5, 'Sarah', 'Connor', 'tech.sarah@meridian.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xD0m1bC.I8d6F./K', '+1 555-9005', 'TECHNICIAN', NULL),
(6, 'John', 'Client', 'customer@acme.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xD0m1bC.I8d6F./K', '+1 555-9006', 'CUSTOMER', 1),
(7, 'Mark', 'Customer', 'customer@apex.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xD0m1bC.I8d6F./K', '+1 555-9007', 'CUSTOMER', 2);

-- 4. Inventory Parts
INSERT INTO part (id, name, sku, unit_cost, stock_qty, created_at) VALUES
(1, 'HVAC Compressor 3HP 410A', 'PRT-HVAC-3HP', 450.00, 12, CURRENT_TIMESTAMP),
(2, 'Copper Tubing 1/2 in (50ft)', 'PRT-COPPER-50', 65.50, 25, CURRENT_TIMESTAMP),
(3, 'Industrial Circuit Breaker 3-Pole 50A', 'PRT-ELE-BRK50', 85.00, 18, CURRENT_TIMESTAMP),
(4, 'Refrigerant R410A Tank 25lb', 'PRT-REF-R410A', 120.00, 8, CURRENT_TIMESTAMP),
(5, 'High-Efficiency Air Filter 20x25x4', 'PRT-FLT-2025', 18.75, 45, CURRENT_TIMESTAMP),
(6, 'Commercial Water Pump Seal Kit', 'PRT-PLM-PUMP1', 34.20, 15, CURRENT_TIMESTAMP);

-- 5. Work Orders
INSERT INTO work_order (id, code, title, description, priority, status, customer_id, site_id, assigned_to_id, sla_due_date, sla_breached, created_at, updated_at) VALUES
(1, 'WO-2026-0001', 'Chiller Unit #3 Leaking Refrigerant', 'HVAC system on 4th floor cooling loop lost pressure overnight. Error Code E-404.', 'URGENT', 'IN_PROGRESS', 1, 1, 4, DATEADD('HOUR', 4, CURRENT_TIMESTAMP), FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

(2, 'WO-2026-0002', 'Server Room Air Handler Overheating', 'Temperature in East Wing Server Closet reached 82F. Emergency dispatch requested.', 'URGENT', 'ASSIGNED', 1, 2, 5, DATEADD('HOUR', 2, CURRENT_TIMESTAMP), FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

(3, 'WO-2026-0003', 'Main Bay Lighting Panel Breaker Trip', 'Breaker #14 trips continuously upon reset. Possible ground fault in warehouse aisle 4.', 'HIGH', 'ON_HOLD', 2, 3, 4, DATEADD('HOUR', -5, CURRENT_TIMESTAMP), TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

(4, 'WO-2026-0004', 'Quarterly HVAC Filter & Belt Service', 'Routine scheduled maintenance across all rooftop AHUs for Building 1.', 'LOW', 'NEW', 1, 1, NULL, DATEADD('HOUR', 72, CURRENT_TIMESTAMP), FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

(5, 'WO-2026-0005', 'Plumbing Backflow Preventer Test & Seal', 'Annual municipal inspection required for main water inlet valve.', 'MEDIUM', 'COMPLETED', 3, 4, 5, DATEADD('HOUR', 24, CURRENT_TIMESTAMP), FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

(6, 'WO-2026-0006', 'Executive Suite Thermostat Calibration', 'Temperature sensor reading offset by +5 degrees in Boardroom B.', 'MEDIUM', 'CLOSED', 1, 1, 4, DATEADD('HOUR', -20, CURRENT_TIMESTAMP), FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 6. Work Order Status History
INSERT INTO work_order_status_history (work_order_id, from_status, to_status, changed_by, changed_at, note) VALUES
(1, NULL, 'NEW', 'customer@acme.com', CURRENT_TIMESTAMP, 'Request raised by customer via portal'),
(1, 'NEW', 'ASSIGNED', 'dispatcher@meridian.com', CURRENT_TIMESTAMP, 'Assigned to Tech Alex Taylor'),
(1, 'ASSIGNED', 'IN_PROGRESS', 'tech.alex@meridian.com', CURRENT_TIMESTAMP, 'Arrived on site, commencing pressure diagnostics'),

(2, NULL, 'NEW', 'customer@acme.com', CURRENT_TIMESTAMP, 'Emergency ticket raised'),
(2, 'NEW', 'ASSIGNED', 'dispatcher@meridian.com', CURRENT_TIMESTAMP, 'Assigned to Tech Sarah Connor'),

(3, NULL, 'NEW', 'dispatcher@meridian.com', CURRENT_TIMESTAMP, 'Created from dispatch call'),
(3, 'NEW', 'ASSIGNED', 'dispatcher@meridian.com', CURRENT_TIMESTAMP, 'Assigned to Alex Taylor'),
(3, 'ASSIGNED', 'IN_PROGRESS', 'tech.alex@meridian.com', CURRENT_TIMESTAMP, 'Diagnosed short circuit'),
(3, 'IN_PROGRESS', 'ON_HOLD', 'tech.alex@meridian.com', CURRENT_TIMESTAMP, 'Waiting for special breaker replacement part'),

(5, NULL, 'NEW', 'dispatcher@meridian.com', CURRENT_TIMESTAMP, 'Scheduled task created'),
(5, 'NEW', 'ASSIGNED', 'dispatcher@meridian.com', CURRENT_TIMESTAMP, 'Assigned to Sarah Connor'),
(5, 'ASSIGNED', 'IN_PROGRESS', 'tech.sarah@meridian.com', CURRENT_TIMESTAMP, 'Testing backflow valve'),
(5, 'IN_PROGRESS', 'COMPLETED', 'tech.sarah@meridian.com', CURRENT_TIMESTAMP, 'Test passed, seal kit replaced.'),

(6, NULL, 'NEW', 'customer@acme.com', CURRENT_TIMESTAMP, 'Thermostat issue logged'),
(6, 'NEW', 'ASSIGNED', 'dispatcher@meridian.com', CURRENT_TIMESTAMP, 'Assigned to Alex Taylor'),
(6, 'ASSIGNED', 'IN_PROGRESS', 'tech.alex@meridian.com', CURRENT_TIMESTAMP, 'Re-calibrated sensor'),
(6, 'IN_PROGRESS', 'COMPLETED', 'tech.alex@meridian.com', CURRENT_TIMESTAMP, 'Verification complete'),
(6, 'COMPLETED', 'CLOSED', 'manager@meridian.com', CURRENT_TIMESTAMP, 'Manager signed off and approved close out');

-- 7. Parts Usage
INSERT INTO part_usage (work_order_id, part_id, qty_used, unit_cost, used_at) VALUES
(1, 4, 1, 120.00, CURRENT_TIMESTAMP),
(1, 2, 2, 65.50, CURRENT_TIMESTAMP),
(5, 6, 1, 34.20, CURRENT_TIMESTAMP),
(6, 5, 2, 18.75, CURRENT_TIMESTAMP);

-- 8. Time Logs
INSERT INTO time_log (work_order_id, technician_id, minutes, note, logged_at) VALUES
(1, 4, 120, 'Leak location search and pressure recovery test', CURRENT_TIMESTAMP),
(3, 4, 45, 'Breaker continuity testing and load calculation', CURRENT_TIMESTAMP),
(5, 5, 90, 'Full backflow test and certificate generation', CURRENT_TIMESTAMP),
(6, 4, 30, 'Thermostat offset recalibration and test run', CURRENT_TIMESTAMP);
