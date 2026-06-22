# URL Shortener API

REST API service for creating shortened URLs with user authentication using JWT.

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5-brightgreen)
![Build](https://img.shields.io/badge/CI-GitHub_Actions-blue)

---

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
* JUnit 5
* Mockito
* MockMvc
* GitHub Actions (CI)

---

## Features

### Authentication

* User registration
* User login
* JWT token generation
* Protected endpoints with Spring Security

### URL Management

* Create short URL
* Update short URL
* Redirect by short code
* Get all user URLs
* Get active URLs
* Delete URL
* Click counter

### Error Handling

* Custom exceptions
* Global exception handling with `@RestControllerAdvice`

### Testing

* Unit tests
* Integration tests
* Controller tests with MockMvc
* Automated CI pipeline with GitHub Actions

---

## Project Structure

```plaintext
src/main/java/org/example/urlshortener
```

### auth

* controller
* dto
* entity
* repository
* service

### security

* config
* JwtService
* JwtAuthenticationFilter
* CustomUserDetailsService

### shorturl

* controller
* dto
* entity
* repository
* service

### redirect

* controller
* service

### common

* exception
* config

---

## Database Schema

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

```bash
docker compose up -d
```

Check container:

```bash
docker ps
```

---

## Application Configuration

`application.properties`

```properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

jwt.secret=${JWT_SECRET}

spring.jpa.hibernate.ddl-auto=validate
spring.flyway.enabled=true
spring.jpa.show-sql=true
springdoc.swagger-ui.path=/swagger-ui.html
```

Example environment variables:

```env
DB_URL=jdbc:postgresql://localhost:5432/url_shortener
DB_USERNAME=postgres
DB_PASSWORD=postgres
JWT_SECRET=your-super-secret-key
```

---

## Run Application

### Windows

```bash
gradlew.bat bootRun
```

### Linux / Mac

```bash
./gradlew bootRun
```

---

## Swagger / OpenAPI

```text
http://localhost:8080/swagger-ui.html
```

---

# API Endpoints

## Authentication

### Register

`POST /api/v1/auth/register`

```json
{
  "username": "roman",
  "password": "Password123"
}
```

### Login

`POST /api/v1/auth/login`

```json
{
  "username": "roman",
  "password": "Password123"
}
```

Response:

```json
{
  "token": "jwt-token"
}
```

---

## Short URLs

### Create

`POST /api/v1/urls`

### Get All

`GET /api/v1/urls`

### Get Active

`GET /api/v1/urls/active`

### Update

`PUT /api/v1/urls/{id}`

### Delete

`DELETE /api/v1/urls/{id}`

Authorization required:

```text
Bearer <JWT_TOKEN>
```

---

## Redirect

### Redirect by short code

`GET /r/{shortCode}`

Example:

```text
http://localhost:8080/r/abc12345
```

Returns HTTP redirect to the original URL.

---

## Testing

Run tests:

```bash
./gradlew test
```

### Unit Tests

* ShortUrlServiceTest
* RedirectServiceTest

### Integration Tests

* AuthControllerTest
* ShortUrlControllerTest
* RedirectControllerTest

---

## CI / GitHub Actions

GitHub Actions automatically runs:

* Build
* Unit tests
* Integration tests

On every:

* push
* pull request

---

## Build

```bash
./gradlew clean build
```

---

## Author

Roman Hnatiak

This project was created for learning and practicing:

* Spring Boot
* Spring Security
* JWT
* PostgreSQL
* Testing
* CI/CD