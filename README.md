# Project KEYSTONE — Field Service Management Platform

[![Java 21](https://img.shields.io/badge/Java-21-orange.svg)](https://oracle.com/java)
[![Spring Boot 3](https://img.shields.io/badge/Spring%20Boot-3.3.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-18.2-blue.svg)](https://react.dev)
[![TypeScript](https://img.shields.io/badge/TypeScript-5.2-blue.svg)](https://www.typescriptlang.org/)
[![Flyway](https://img.shields.io/badge/Flyway-Migrations-red.svg)](https://flywaydb.org)

> **Client:** Meridian Facilities Management  
> **Engineering Brief:** Java Full-Stack Practice (Zidio Development)

---

## 📌 Executive Summary

**KEYSTONE** is an end-to-end field-service platform engineered for **Meridian Facilities Management** to streamline commercial HVAC, electrical, and plumbing operations. KEYSTONE replaces fragmented spreadsheet workflow with a single, highly governed system of record:

- **Dispatchers** create work orders, assign field engineers by skill/workload, and track jobs on an interactive Kanban board.
- **Technicians** manage assigned jobs from mobile devices, record real-time state transitions, log parts consumed, and track labor hours.
- **Managers** oversee operational SLA compliance, handle sign-off closeouts, manage spare parts inventory, and analyze live performance dashboards.
- **Customers** self-serve maintenance requests for their facility sites and monitor ticket progress in real-time.

---

## 🛠️ Technology Stack

| Layer | Technology | Purpose |
| :--- | :--- | :--- |
| **Language** | Java 21 LTS | Modern LTS Java syntax, records, pattern matching |
| **Backend Framework** | Spring Boot 3.3.0 (Web, Validation) | Layered REST API microservice |
| **Security** | Spring Security + Stateless JWT | Method-level `@PreAuthorize` RBAC & JWT bearer tokens |
| **Persistence** | Spring Data JPA / Hibernate | Object-Relational Mapping & transactional boundaries |
| **Database** | PostgreSQL / H2 Database | Reliable relational data store (H2 in-memory default for 1-click zero setup) |
| **Migrations** | Flyway DB Migration | Version-controlled DB schema (`V1`) and seed data (`V2`) |
| **API Documentation** | OpenAPI 3.0 / Swagger UI | Live interactive API test console (`/swagger-ui.html`) |
| **Frontend SPA** | React 18 + TypeScript + Vite | Component-based SPA with Glassmorphism UX |

---

## 🏗️ System Architecture & Domain Model

```
                    ┌─────────────────────────────────────────┐
                    │          React SPA (Vite + TS)          │
                    │  Dashboard | Kanban | Tech | Customer   │
                    └────────────────────┬────────────────────┘
                                         │ HTTP REST / JWT
                                         ▼
                    ┌─────────────────────────────────────────┐
                    │      Spring Boot Controllers (REST)     │
                    │  Thin Controllers / Validation / DTOs   │
                    └────────────────────┬────────────────────┘
                                         │
                                         ▼
                    ┌─────────────────────────────────────────┐
                    │              Service Layer              │
                    │ State Machine / SLA Rules / Transaction │
                    └────────────────────┬────────────────────┘
                                         │
                                         ▼
                    ┌─────────────────────────────────────────┐
                    │           Spring Data JPA Repos         │
                    └────────────────────┬────────────────────┘
                                         │
                                         ▼
                    ┌─────────────────────────────────────────┐
                    │        PostgreSQL / H2 Database         │
                    │         (Flyway Migration Engine)       │
                    └─────────────────────────────────────────┘
```

### Core Domain Entities & Relationships
- `Customer`: Client organization owning multiple facility sites.
- `Site`: Physical location where maintenance work happens.
- `UserAuth`: Platform user assigned one of 4 roles (`DISPATCHER`, `TECHNICIAN`, `MANAGER`, `CUSTOMER`).
- `WorkOrder`: Core unit of work (`code`, `priority`, `status`, `customer`, `site`, `assignedTo`, `slaDueDate`, `slaBreached`).
- `WorkOrderStatusHistory`: **Append-only** state transition audit trail (`fromStatus`, `toStatus`, `changedBy`, `changedAt`, `note`).
- `Part`: Inventory item with SKU, stock quantity, and unit cost.
- `PartUsage`: Quantity of parts consumed on a work order (**Transactionally decrements stock**).
- `TimeLog`: Labor minutes logged by field technicians against a work order.

---

## 🔄 Governed Work-Order Lifecycle (State Machine)

Work orders transition through strict, validated state machine rules:

```
[ NEW ] ───(assign)───► [ ASSIGNED ] ───(start)───► [ IN_PROGRESS ] ───(complete)───► [ COMPLETED ] ───(close)───► [ CLOSED ]
   │                        │                            │                                                            (Terminal)
   │                        │                      (hold)│▲(resume)
   │                        │                            ▼│
   │                        └───────────────────────► [ ON_HOLD ]
   │
   └─────────────────────────────────────────────────► [ CANCELLED ] (Terminal)
```

- **Illegal Jump Protection:** Transitions skipping required states (e.g. `NEW` -> `COMPLETED`) are rejected with `409 Conflict`.
- **Role Enforcement:**
  - Technicians can only transition jobs assigned to them (`ASSIGNED` -> `IN_PROGRESS`, `ON_HOLD`, `COMPLETED`).
  - Only Managers can sign off and close completed jobs (`COMPLETED` -> `CLOSED`).
  - Customers can only view their own organization's requests.

---

## 🔑 User Roles & Seed Credentials

The database is pre-seeded with sample users for instant testing. Use the **Demo Fast Role Switcher** bar at the top of the UI to switch between roles instantly:

| Role | Email | Password | Permissions |
| :--- | :--- | :--- | :--- |
| **Manager / Admin** | `manager@meridian.com` | `password123` | Full access, close jobs, manage parts, view SLA metrics |
| **Dispatcher** | `dispatcher@meridian.com` | `password123` | Create work orders, assign/reassign technicians, manage customers & sites |
| **Technician 1** | `tech.alex@meridian.com` | `password123` | View assigned field jobs, start/hold/complete work, log parts & time |
| **Technician 2** | `tech.sarah@meridian.com` | `password123` | View assigned field jobs, start/hold/complete work, log parts & time |
| **Customer 1** | `customer@acme.com` | `password123` | Raise requests for Acme Commercial, track status & history |
| **Customer 2** | `customer@apex.com` | `password123` | Raise requests for Apex Logistics, track status & history |

---

## 🚀 Quick Start & How to Run

### 1. Prerequisites
- **JDK 21** or later
- **Node.js 18+** & npm

### 2. Run the Backend (Spring Boot)
From the root directory:
```bash
./mvnw clean spring-boot:run
```
The server will start at **http://localhost:8080**.
- **Swagger UI API Documentation:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **H2 Console:** [http://localhost:8080/h2-console](http://localhost:8080/h2-console) (JDBC URL: `jdbc:h2:mem:keystonedb`)

### 3. Run the Frontend (Vite Dev Server)
In a separate terminal:
```bash
cd frontend
npm install
npm run dev
```
Open **http://localhost:3000** in your browser.

---

## 📄 Key REST API Endpoints

- `POST /api/auth/login` - Authenticate user & receive JWT token
- `GET /api/work-orders` - List work orders (role-scoped, filterable, paginated)
- `POST /api/work-orders` - Create work order ticket
- `GET /api/work-orders/{id}` - Fetch details, history, parts, and time logs
- `POST /api/work-orders/{id}/assign` - Assign technician
- `POST /api/work-orders/{id}/status` - Transition work order status (State Machine guarded)
- `POST /api/work-orders/{id}/parts` - Log part used (**Transactionally decrements stock**)
- `POST /api/work-orders/{id}/time` - Log labor minutes
- `GET /api/reports/summary` - Manager operational metrics & SLA compliance report
