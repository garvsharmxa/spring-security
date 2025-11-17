# Spring Security JWT Authentication

A production-ready JWT authentication system for Spring Boot applications with proper REST APIs.

## Features

- User registration and login
- JWT access tokens (1 hour) and refresh tokens (7 days)
- Token refresh without re-authentication
- Secure logout and password encryption
- Proper HTTP status codes and JSON responses
- Global exception handling

## Quick Start

### Prerequisites
- Java 17+
- Maven 3.6+
- PostgreSQL

### Setup

1. **Clone and configure database**
```bash
git clone https://github.com/garvsharmxa/spring-security.git
cd spring-security
```

Edit `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/your_database
spring.datasource.username=your_username
spring.datasource.password=your_password
```

2. **Set JWT secret**
```bash
export JWT_SECRET="your-secure-secret-key-min-32-characters"
```

3. **Run**
```bash
./mvnw clean package
java -jar target/SpringSecEx-0.0.1-SNAPSHOT.jar
```

## API Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/auth/register` | Register new user | No |
| POST | `/auth/login` | Login and get tokens | No |
| POST | `/auth/refresh` | Refresh access token | No |
| POST | `/auth/logout` | Logout user | Yes |

### Example Usage

**Register:**
```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"testpass123"}'
```

**Login:**
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"testpass123"}'
```

**Response:**
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "accessToken": "eyJhbGc...",
    "refreshToken": "eyJhbGc...",
    "username": "testuser",
    "tokenType": "Bearer",
    "expiresIn": 3600000
  }
}
```

## Documentation

- **[AUTH_MODULE_USAGE.md](AUTH_MODULE_USAGE.md)** - Complete API documentation
- **[INTEGRATION_GUIDE.md](INTEGRATION_GUIDE.md)** - Integration instructions
- **[API_EXAMPLES.md](API_EXAMPLES.md)** - Response examples
- **[test-api.sh](test-api.sh)** - Automated testing script

## Tech Stack

- Spring Boot 3.5.7
- Spring Security
- JWT (jjwt 0.13.0)
- BCrypt password encryption
- PostgreSQL

## Security Features

- BCrypt password hashing
- JWT token validation
- Token expiration handling
- Stateless session management
- Global exception handling

## Testing

Run automated tests:
```bash
chmod +x test-api.sh
./test-api.sh
```

## Integration

Copy these packages to your project:
- `dto/`, `exception/`, `Utlity/`, `Services/`
- `Controller/AuthController.java`
- `Entity/`, `Repository/`, `Conig/SecurityConfig.java`

See [INTEGRATION_GUIDE.md](INTEGRATION_GUIDE.md) for detailed steps.

## Configuration

**Token expiration** (in `JwtUtil.java`):
```java
private final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hour
private final long REFRESH_TOKEN_EXPIRATION_TIME = 7 * 24 * 60 * 60 * 1000L; // 7 days
```

**Security settings** (in `SecurityConfig.java`):
```java
.requestMatchers("/auth/register", "/auth/login", "/auth/refresh").permitAll()
.anyRequest().authenticated()
```

## License

Available for use in your projects.

---

Perfect for RESTful APIs, microservices, mobile backends, and SPAs.
