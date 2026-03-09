# 🗓️ Schedula API
> A high-performance **RESTful API** built with **Spring Boot** and **PostgreSQL** for structured task scheduling and resource management.

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Relational-blue)
![Swagger](https://img.shields.io/badge/Documentation-Swagger-blue)

---

## 🚀 Project Overview
**Schedula** is a dedicated backend service designed for efficient scheduling operations. It utilizes a **Relational Database (PostgreSQL)** to ensure data integrity and complex relationship mapping between users, tasks, and schedules. The API is fully documented and testable via **Swagger UI**.



## 🛠 Tech Stack
* **Backend:** Java 17, Spring Boot 3.x
* **Database:** **PostgreSQL** (Relational Database Management System)
* **ORM:** Spring Data JPA / Hibernate
* **API Documentation:** **Swagger UI / SpringDoc** (OpenAPI 3)
* **Security:** Spring Security & JWT (Stateless Authentication)
* **Build Tool:** Maven

## ✨ Key Features
* 📖 **Interactive API Docs:** Full documentation and testing sandbox via Swagger.
* 🛡 **Data Integrity:** Strict relational constraints and ACID compliance via PostgreSQL.
* 🔐 **JWT Security:** Secure, stateless authentication for all protected routes.
* 🏗 **Structured Schema:** Clear entity-relationship mapping for complex scheduling logic.
* 🚀 **CORS Enabled:** Seamless integration with Angular or Flutter applications.

## ⚙️ Getting Started

### Prerequisites
* **JDK:** 17 or higher
* **Database:** PostgreSQL 14+
* **Build Tool:** Maven

### Installation & Setup
1.  **Clone the Repository:**
    ```bash
    git clone [https://github.com/MohammedGhallab/schedula.git](https://github.com/MohammedGhallab/schedula.git)
    cd schedula
    ```

2.  **Configure Environment:**
    Edit `src/main/resources/application.properties` with your PostgreSQL credentials:
    ```properties
    # PostgreSQL Configuration
    spring.datasource.url=jdbc:postgresql://localhost:5432/schedula_db
    spring.datasource.username=your_username
    spring.datasource.password=your_password
    
    # Hibernate / JPA Settings
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.show-sql=true
    
    # Server Port
    server.port=8081
    ```

3.  **Build and Run:**
    ```bash
    mvn clean install
    mvn spring-boot:run
    ```

## 📖 API Documentation (Swagger)
Explore and test the API endpoints directly from your browser:

🔗 **Swagger UI:** `http://localhost:8081/swagger-ui/index.html`



## 🔌 Core Endpoints
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/auth/login` | Authenticate & get JWT Token |
| `GET` | `/appointments` | Fetch all scheduled entries |
| `POST` | `/appointments` | Add a new schedule record |
| `PUT` | `/appointments/{id}` | Update an existing schedule |

## 🛡 Security
This API uses **JWT**. To test protected routes in Swagger:
1. Login via `/auth/login` to receive your token.
2. Click **"Authorize"** in Swagger UI.
3. Use the format: `Bearer <your_token>`.

## 📄 License
This project is licensed under the **MIT License**.

---
**Maintained by [Mohammed Ghallab](https://github.com/MohammedGhallab)**
