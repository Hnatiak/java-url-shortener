# URL Shortener API

REST API service for creating shortened URLs with user authentication using JWT.

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
* CI pipeline with GitHub Actions

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

Start database:

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

Request:

```json
{
  "username": "roman",
  "password": "Password123"
}
```

Response:

```json
{
  "token": "User successfully registered"
}
```

---

### Login

`POST /api/v1/auth/login`

Request:

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

### Create Short URL

`POST /api/v1/urls`

Authorization:

```text
Bearer Token
```

Request:

```json
{
  "originalUrl": "https://google.com",
  "expiresAt": "2027-12-31T23:59:59"
}
```

---

### Get All URLs

`GET /api/v1/urls`

Authorization required.

---

### Get Active URLs

`GET /api/v1/urls/active`

Authorization required.

---

### Update URL

`PUT /api/v1/urls/{id}`

Authorization required.

Request:

```json
{
  "originalUrl": "https://youtube.com",
  "expiresAt": "2028-12-31T23:59:59"
}
```

---

### Delete URL

`DELETE /api/v1/urls/{id}`

Authorization required.

---

## Redirect

### Redirect by short code

`GET /r/{shortCode}`

Example:

```text
http://localhost:8080/r/abc12345
```

Returns HTTP redirect to original URL.

---

## Testing

Run all tests:

```bash
./gradlew test
```

Implemented tests:

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

Java URL Shortener project created for learning:

* Spring Boot
* Spring Security
* JWT
* PostgreSQL
* Testing
* CI/CD