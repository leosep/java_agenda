# java_agenda
Simple contact book REST API using Java, Spring Boot, PostgreSQL and Swagger, applying DDD, Clean Code and principles such as DRY.

# 🚀 Contact Agenda REST API

A simple RESTful API for managing personal contacts, built with **Spring Boot 3.x**, **Java 21 (LTS)**, **PostgreSQL**, and documented with **SpringDoc OpenAPI (Swagger)**. This project follows **Domain-Driven Design (DDD)** principles, **Clean Code** practices, and the **DRY (Don't Repeat Yourself)** principle.

---

## ✨ Features

* **CRUD Operations:** Create, Read, Update, Delete contacts.
* **Data Validation:** Robust input validation for contact details (name, phone, email).
* **Unique Constraints:** Prevents duplicate contacts based on email and phone number.
* **RESTful Design:** Standard HTTP methods and status codes.
* **Database Persistence:** Uses PostgreSQL via Spring Data JPA.
* **Interactive Documentation:** Auto-generated API documentation with Swagger UI.
* **Clean Architecture:** Organized into distinct layers (Domain, Application, Infrastructure, Interfaces) for maintainability and scalability.

---

## 🛠️ Technologies Used

* **Java 21 (LTS):** The core programming language.
* **Spring Boot 3.3.1+:** Framework for building the REST API.
* **Spring Data JPA:** For database interaction and object-relational mapping.
* **PostgreSQL:** Relational database for storing contacts.
* **Lombok:** Reduces boilerplate code (getters, setters, constructors).
* **SpringDoc OpenAPI (Swagger UI):** For API documentation and testing.
* **Maven:** Project build automation and dependency management.
* **Validation API (Jakarta Validation):** For data validation.

---

## ⚙️ Setup and Installation

### Prerequisites

* **Java Development Kit (JDK) 21 (LTS)**
* **Apache Maven 3.6+**
* **PostgreSQL 12+**
* **IntelliJ IDEA** (or any other compatible IDE like VS Code, Eclipse)

### 1. Database Configuration

1.  **Create PostgreSQL Database:**
    Ensure you have a PostgreSQL database named `agenda_db` created.
    ```sql
    CREATE DATABASE agenda_db;
    CREATE USER your_postgres_user WITH PASSWORD 'your_postgres_password';
    GRANT ALL PRIVILEGES ON DATABASE agenda_db TO your_postgres_user;
    ```
    (Replace `your_postgres_user` and `your_postgres_password` with your desired credentials.)

2.  **Update `application.properties`:**
    Open `src/main/resources/application.properties` and configure your database connection details:

    ```properties
    spring.datasource.url=jdbc:postgresql://localhost:5432/agenda_db
    spring.datasource.username=your_postgres_user
    spring.datasource.password=your_postgres_password
    spring.datasource.driver-class-name=org.postgresql.Driver
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.show-sql=true
    spring.jpa.properties.hibernate.format_sql=true

    # Swagger/OpenAPI Configuration
    springdoc.swagger-ui.path=/swagger-ui.html
    springdoc.api-docs.path=/v3/api-docs
    springdoc.packages-to-scan=com.agenda.contactos.interfaces.rest
    springdoc.paths-to-match=/api/**

    # Server Port (optional, default is 8080)
    server.port=8080
    ```

### 2. Build and Run the Application

1.  **Clone the Repository**


2.  **Build with Maven:**
    Navigate to the project root directory in your terminal (where `pom.xml` is located) and run:
    ```bash
    mvn clean install
    ```

3.  **Run the Spring Boot Application:**
    ```bash
    mvn spring-boot:run
    ```
    The application will start on `http://localhost:8080` (or your configured port).

---

## 📖 API Documentation (Swagger UI)

Once the application is running, you can access the interactive API documentation and test endpoints through Swagger UI:

[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## 💡 API Endpoints

All endpoints are prefixed with `/api/v1/contacts`.

### **`POST /api/v1/contacts`** - Create a new contact

* **Request Body:** `ContactCreateRequest`
    ```json
    {
        "name": "John Doe",
        "phone": "+1234567890",
        "email": "john.doe@example.com"
    }
    ```
* **Responses:**
    * `201 Created`: `ContactResponse` object
    * `400 Bad Request`: Invalid input data (validation errors)
    * `409 Conflict`: Contact with given email or phone already exists

### **`GET /api/v1/contacts/{id}`** - Get contact by ID

* **Path Variable:** `id` (Long)
* **Responses:**
    * `200 OK`: `ContactResponse` object
    * `404 Not Found`: Contact not found

### **`GET /api/v1/contacts`** - Get all contacts

* **Responses:**
    * `200 OK`: List of `ContactResponse` objects

### **`PUT /api/v1/contacts/{id}`** - Update an existing contact

* **Path Variable:** `id` (Long)
* **Request Body:** `ContactUpdateRequest` (fields are optional for partial updates)
    ```json
    {
        "name": "Johnny D.",
        "phone": "+1987654321"
    }
    ```
* **Responses:**
    * `200 OK`: `ContactResponse` object
    * `400 Bad Request`: Invalid input data (validation errors)
    * `404 Not Found`: Contact not found
    * `409 Conflict`: Another contact with the updated email or phone already exists

### **`DELETE /api/v1/contacts/{id}`** - Delete a contact by ID

* **Path Variable:** `id` (Long)
* **Responses:**
    * `204 No Content`: Contact deleted successfully
    * `404 Not Found`: Contact not found