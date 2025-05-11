# 📄 DocuQuery – Document Ingestion & Question Answering System

DocuQuery is a Spring Boot-based backend system that enables document ingestion (PDF, DOCX), metadata filtering, and keyword-based question answering using Apache Tika and PostgreSQL. Swagger UI is provided for API testing and documentation.

---

## 🚀 Features

- 📅 Upload and extract document content (PDF, Word)

- 🔎 Basic and enhanced keyword-based question answering

- 🧠 Metadata filtering with pagination & sorting

- 📈 Swagger UI for interactive API testing

- 🧪 JUnit 5: For unit testing

- 🦹‍♂️ Mockito: For mocking services during unit testing

- ⛓️ JWT Authentication with Role-based Access Control

- ⚡ Caching: Redis caching integrated for query results, improving performance

---

## 🛠️ Tech Stack

| Layer      | Technology                        |
|------------|-----------------------------------|
| Backend    | Java 17, Spring Boot              |
| ORM        | Spring Data JPA                   |
| Database   | PostgreSQL                        |
| File Parser| Apache Tika                       |
| Docs UI    | Springdoc OpenAPI (v2.x) + Swagger|
| Auth       | JWT (JSON Web Token)              |
| Build Tool | Maven                             |
| Utilities  | Lombok                            |

---

## 🔗 API Endpoints

### 📄 Upload Document

**POST** `/api/documents/upload` (Requires `ADMIN` role)

**Request**: `MultipartFile` file

**Response**:
```json
{
  "id": 1,
  "title": "Sample Title",
  "author": "Author Name",
  "content": "Extracted text...",
  "type": "pdf",
  "uploadDate": "2025-05-04"
}
```

---

### 🔍 Search v1 (Basic)

**GET** `/api/documents/v1/qa?query=your+query` (Requires `VIEWER` role)

**Response**:
```json
[
  {
    "id": 1,
    "title": "Document Title",
    "author": "Author",
    "content": "Matched snippet..."
  }
]
```

---

### 🔎 Search v2 (Enhanced)

**GET** `/api/documents/v2/qa?query=your+query` (Public Access)

**Response**: Same as v1.

---

### 📆 Filter Documents

**GET** `/api/documents/filter` (Requires `EDITOR` role)

**Query Parameters:**

| Parameter        | Description                     |
|------------------|---------------------------------|
| `author`         | Filter by author               |
| `type`           | File type (e.g., pdf)          |
| `uploadDateFrom` | Start date (yyyy-MM-dd)        |
| `uploadDateTo`   | End date (yyyy-MM-dd)          |
| `page`           | Page number (default = 0)      |
| `size`           | Page size (default = 10)       |
| `sort`           | Format: `field,direction`      |

**Example**:
```
/api/documents/filter?author=John&type=pdf&uploadDateFrom=2024-01-01&uploadDateTo=2025-01-01&page=0&size=5&sort=title,asc
```

**Response**:
```json
{
  "content": [...],
  "totalElements": 12,
  "totalPages": 3,
  "size": 5,
  "number": 0
}
```

---

## 🗓️ Authentication & Authorization (JWT)

### Login
**POST** `/api/auth/login`

**Request:**
```json
{
  "username": "admin",
  "password": "admin123"
}
```

**Response:**
```json
{
  "token": "<JWT_TOKEN>",
  "userName":"<name>",
  "role":"[<role>]"
}
```

### Role-Based Access
| Endpoint                       | Required Role |
|--------------------------------|---------------|
| `/api/documents/upload`       | `ROLE_ADMIN`  |
| `/api/documents/filter`       | `ROLE_EDITOR` |
| `/api/documents/v1/qa`        | `ROLE_VIEWER` |
| `/api/documents/v2/qa`        | Public        |

**Note:** Roles are stored in DB with prefix `ROLE_`, but matched as `hasRole("ADMIN")`, etc.

---

## 📅 Swagger UI (OpenAPI Docs)

Accessible at: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

**Springdoc Dependency (Maven):**
```xml
<dependency>
  <groupId>org.springdoc</groupId>
  <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
  <version>2.1.0</version>
</dependency>
```

This provides:
- Interactive API documentation
- Example responses & request models

---

## 🌐 How to Run

### ✅ Prerequisites
- Java 17+
- Maven
- PostgreSQL

### ⚙️ Steps
1. Clone repo & navigate:
```bash
git clone https://github.com/your-username/docuquery.git
cd docuquery
```
2. Configure PostgreSQL in `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/docuquery_db
spring.datasource.username=postgres
spring.datasource.password=your_password
```
3. Build & run:
```bash
mvn clean install
mvn spring-boot:run
```

---
