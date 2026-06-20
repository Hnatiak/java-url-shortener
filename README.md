# URL Shortener API

REST API сервіс для створення скорочених URL-посилань з авторизацією користувачів через JWT.

## Technologies

* Java 21
* Spring Boot 3.5
* Spring Security
* JWT Authentication
* Spring Data JPA
* PostgreSQL
* Flyway
* Docker
* Swagger / OpenAPI
* Lombok
* Gradle

---

## Features

### Authentication

* User registration
* User login
* JWT token generation
* Protected endpoints

### URL Management

* Create short URL
* Redirect by short code
* Get all user URLs
* Get active URLs
* Delete URL
* Click counter

---

## Project Structure

src/main/java/org/example/urlshortener

* auth

  * controller
  * dto
  * entity
  * repository
  * service

* security

  * config
  * JwtService
  * JwtAuthenticationFilter
  * CustomUserDetailsService

* shorturl

  * controller
  * dto
  * entity
  * repository
  * service

---

## Database

### users

| Column     | Type      |
| ---------- | --------- |
| id         | BIGSERIAL |
| username   | VARCHAR   |
| password   | VARCHAR   |
| created_at | TIMESTAMP |

### short_urls

| Column       | Type       |
| ------------ | ---------- |
| id           | BIGSERIAL  |
| original_url | TEXT       |
| short_code   | VARCHAR(8) |
| click_count  | BIGINT     |
| created_at   | TIMESTAMP  |
| expires_at   | TIMESTAMP  |
| user_id      | BIGINT     |

---

## Running PostgreSQL

docker compose up -d

Check container:

docker ps

---

## Application Configuration

application.properties

spring.datasource.url=jdbc:postgresql://localhost:5432/url_shortener

spring.datasource.username=postgres

spring.datasource.password=postgres

---

## Run Application

Windows:

gradlew.bat bootRun

Linux / Mac:

./gradlew bootRun

---

## Swagger

http://localhost:8080/swagger-ui.html

---

## API Endpoints

### Register

POST /api/v1/auth/register

Request

```json
{
  "username": "roman",
  "password": "Password123"
}
```

### Login

POST /api/v1/auth/login

Request

```json
{
  "username": "roman",
  "password": "Password123"
}
```

Response

```json
{
  "token": "jwt-token"
}
```

### Create Short URL

POST /api/v1/urls

Authorization:

Bearer Token

```json
{
  "originalUrl": "https://google.com",
  "expiresAt": "2026-12-31T23:59:59"
}
```

### Get All URLs

GET /api/v1/urls

Authorization:

Bearer Token

### Get Active URLs

GET /api/v1/urls/active

Authorization:

Bearer Token

### Delete URL

DELETE /api/v1/urls/{id}

Authorization:

Bearer Token

### Redirect

GET /{shortCode}

Example:

http://localhost:8080/OyEg1Sp2

---

## Build

./gradlew clean build

---

## Author

Roman Hnatiak

Java URL Shortener project created for learning Spring Boot, Security, JWT and PostgreSQL.