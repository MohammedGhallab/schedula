# 🗓️ Schedula

> **Schedula** is a robust, high-performance **RESTful API** designed for modern appointment scheduling and resource management. Built with **Spring Boot 3** and **Java 23**, it leverages **PostgreSQL** for reliable data persistence and **Redis** for efficient caching, providing a seamless experience for developers and end-users alike.

[![Java 23](https://img.shields.io/badge/Java-23-orange?logo=openjdk)](https://openjdk.org/projects/jdk/23/)
[![Spring Boot 3.4.1](https://img.shields.io/badge/Spring_Boot-3.4.1-brightgreen?logo=springboot)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Relational-blue?logo=postgresql)](https://www.postgresql.org/)
[![Redis](https://img.shields.io/badge/Redis-Caching-red?logo=redis)](https://redis.io/)
[![Swagger](https://img.shields.io/badge/API-Swagger-blue?logo=swagger)](https://swagger.io/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

---

## 🚀 Overview

**Schedula** is more than just a scheduling tool; it's a comprehensive backend solution for service providers and clients. It features a secure, scalable architecture optimized for performance and data integrity.

### ✨ Key Features

- 🔐 **Advanced Security:** Stateless authentication using **JWT** (JSON Web Tokens) and **Spring Security**.
- 📅 **Appointment Management:** Sophisticated logic for booking, updating, and tracking appointments.
- 💳 **Payment Integration:** Ready-to-use module for handling payments and transactions.
- 🔔 **Real-time Notifications:** Integrated notification system for appointment reminders and status updates.
- 🏢 **Multi-Provider Support:** Designed to handle multiple service providers and their unique schedules.
- ⚡ **Performance Optimized:** Utilizing **Redis** for caching and **Spring Data JPA** with **PostgreSQL** for efficient data access.
- 📖 **Interactive Documentation:** Fully documented API endpoints with **Swagger UI**.

---

## 🛠 Tech Stack

| Category | Technology |
| :--- | :--- |
| **Core** | Java 23, Spring Boot 3.4.1 |
| **Database** | PostgreSQL (Relational) |
| **Caching** | Redis |
| **Security** | Spring Security, JWT |
| **ORM** | Spring Data JPA / Hibernate |
| **Mapping** | MapStruct |
| **API Docs** | SpringDoc OpenAPI (Swagger UI) |
| **Build Tool** | Maven |

---

## ⚙️ Getting Started

### Prerequisites

- **JDK 23** or higher
- **PostgreSQL 14+**
- **Redis** server
- **Maven** 3.8+

### Installation

1. **Clone the Repository:**
   ```bash
   git clone https://github.com/MohammedGhallab/schedula.git
   cd schedula
   ```

2. **Configure Environment:**
   Update your PostgreSQL and Redis credentials in `src/main/resources/application.properties` (or use environment variables).
   ```properties
   # Database
   spring.datasource.url=jdbc:postgresql://localhost:5432/schedula_db
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   
   # Redis
   spring.data.redis.host=localhost
   spring.data.redis.port=6379
   ```

3. **Build & Run:**
   ```bash
   ./mvnw clean install
   ./mvnw spring-boot:run
   ```

---

## 📖 API Documentation

Explore and test the API endpoints directly using the **Swagger UI**:

🔗 **Swagger UI:** `http://localhost:8081/swagger-ui/index.html`

### Core Modules

- **Auth:** `/api/v1/auth` - Login and Registration.
- **Appointments:** `/api/v1/appointments` - Schedule and manage bookings.
- **Users:** `/api/v1/users` - User profile management.
- **Payments:** `/api/v1/payments` - Transaction handling.

---

## 📁 Project Structure

```text
src/main/java/com/schedula/schedula/
├── appointment/       # Appointment management logic
├── audit/             # Auditing and logging
├── config/            # System configurations (Security, Redis, etc.)
├── core/              # Core business entities and logic
├── payment/           # Payment processing
├── notification/      # Notification system
├── user/              # User management and authentication
└── utils/             # Helper utilities
```

---

## 🛡 Security

To access protected endpoints:
1. Register/Login via the Auth controller.
2. Copy the `access_token` from the response.
3. In Swagger UI, click **Authorize** and enter: `Bearer <your_token>`.

---

## 📄 License

Distributed under the MIT License. See `LICENSE` for more information.

---

**Developed & Maintained by [Mohammed Ghallab](https://github.com/MohammedGhallab)**
