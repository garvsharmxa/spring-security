# JWT Authentication Module - Usage Guide

## Overview
This is a reusable JWT-based authentication module for Spring Boot applications. It provides complete user authentication with access tokens, refresh tokens, and proper REST API responses with status codes.

## Features
- ✅ User Registration
- ✅ User Login with JWT tokens
- ✅ Access Token (1 hour validity)
- ✅ Refresh Token (7 days validity)
- ✅ Token Refresh endpoint
- ✅ Logout functionality
- ✅ Proper HTTP status codes
- ✅ Structured JSON responses
- ✅ Global exception handling
- ✅ BCrypt password encryption

## Prerequisites
- Java 17+
- PostgreSQL database
- Maven

## Configuration

### 1. Database Setup
Configure your PostgreSQL database in `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/your_database
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

### 2. JWT Secret Key
Set the JWT secret key (minimum 32 characters):

```properties
jwt.secret=your-secret-key-min-32-chars-long-1234567890
```

Or use environment variable:
```bash
export JWT_SECRET=your-secret-key-here
```

## API Endpoints

### Base URL: `/auth`

#### 1. Register User
**Endpoint:** `POST /auth/register`

**Request Body:**
```json
{
  "username": "john_doe",
  "password": "securePassword123"
}
```

**Response (201 CREATED):**
```json
{
  "success": true,
  "message": "User registered successfully",
  "data": {
    "id": 1,
    "username": "john_doe"
  },
  "statusCode": 201
}
```

**Error Response (409 CONFLICT):**
```json
{
  "success": false,
  "message": "Username already exists: john_doe",
  "statusCode": 409
}
```

#### 2. Login
**Endpoint:** `POST /auth/login`

**Request Body:**
```json
{
  "username": "john_doe",
  "password": "securePassword123"
}
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiJ9...",
    "username": "john_doe",
    "tokenType": "Bearer",
    "expiresIn": 3600000
  },
  "statusCode": 200
}
```

**Error Response (401 UNAUTHORIZED):**
```json
{
  "success": false,
  "message": "Invalid username or password",
  "statusCode": 401
}
```

#### 3. Refresh Token
**Endpoint:** `POST /auth/refresh`

**Request Body:**
```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiJ9..."
}
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Token refreshed successfully",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiJ9...",
    "username": "john_doe",
    "tokenType": "Bearer",
    "expiresIn": 3600000
  },
  "statusCode": 200
}
```

**Error Response (401 UNAUTHORIZED):**
```json
{
  "success": false,
  "message": "Invalid or expired refresh token",
  "statusCode": 401
}
```

#### 4. Logout
**Endpoint:** `POST /auth/logout`

**Headers:**
```
Authorization: Bearer <access-token>
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Logged out successfully",
  "statusCode": 200
}
```

## Using Protected Endpoints

For any protected endpoint, include the access token in the Authorization header:

```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

Example using curl:
```bash
curl -X GET http://localhost:8080/api/protected \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

## Integration into Your Project

### Step 1: Copy Required Files
Copy the following packages to your project:
- `dto/` - All DTOs for requests/responses
- `exception/` - Custom exceptions and global handler
- `Utlity/` - JWT utilities
- `Services/UserService.java` - Authentication service
- `Controller/AuthController.java` - Authentication endpoints
- `Entity/Users.java` and `UserPrincipal.java` - User entities
- `Repository/UserRepository.java` - User repository
- `Conig/SecurityConfig.java` - Security configuration

### Step 2: Update Dependencies
Ensure your `pom.xml` includes:
```xml
<!-- Spring Security -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<!-- JWT -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.13.0</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.13.0</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.13.0</version>
    <scope>runtime</scope>
</dependency>

<!-- JPA -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```

### Step 3: Configure Application Properties
Add the configuration from the Configuration section above.

### Step 4: Adjust Package Names
Update all package names to match your project structure.

## Token Validity
- **Access Token:** 1 hour (3600000 ms)
- **Refresh Token:** 7 days (604800000 ms)

You can modify these in `JwtUtil.java`:
```java
private final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hour
private final long REFRESH_TOKEN_EXPIRATION_TIME = 7 * 24 * 60 * 60 * 1000L; // 7 days
```

## Security Best Practices

1. **Never commit your JWT secret** - Use environment variables
2. **Use HTTPS in production** - Prevent token interception
3. **Store tokens securely on client** - Use httpOnly cookies or secure storage
4. **Implement token blacklist** (optional) - For logout in distributed systems
5. **Rotate refresh tokens** - Generate new refresh token on each refresh
6. **Add rate limiting** - Prevent brute force attacks
7. **Use strong passwords** - Implement password policy validation

## Testing the Endpoints

### Using curl:

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

**Refresh Token:**
```bash
curl -X POST http://localhost:8080/auth/refresh \
  -H "Content-Type: application/json" \
  -d '{"refreshToken":"YOUR_REFRESH_TOKEN"}'
```

**Logout:**
```bash
curl -X POST http://localhost:8080/auth/logout \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

## Extending the Module

### Adding User Roles
Modify `Users` entity to include roles:
```java
private String role; // USER, ADMIN, etc.
```

Update `UserPrincipal` to return actual roles.

### Adding Email/Phone
Extend `RegisterRequest` and `Users` entity:
```java
private String email;
private String phone;
```

### Adding User Profile
Create additional endpoints in a `ProfileController` for user management.

## Troubleshooting

### Issue: "Invalid or expired token"
- Check if token is expired
- Verify JWT secret is consistent
- Ensure token is properly formatted in Authorization header

### Issue: "User already exists"
- Username must be unique
- Check database for existing user

### Issue: "Invalid credentials"
- Verify username and password are correct
- Check if user exists in database

## License
This module is provided as-is for use in your projects.

## Support
For issues or questions, please refer to the main repository documentation.
