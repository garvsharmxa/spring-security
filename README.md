# Spring Security JWT Authentication Module

A production-ready, reusable JWT-based authentication system for Spring Boot applications with proper REST API responses, status codes, and comprehensive documentation.

## ✨ Features

- ✅ **User Registration** - Create new user accounts with password encryption
- ✅ **User Login** - Authenticate users and generate JWT tokens
- ✅ **Access Tokens** - Short-lived tokens (1 hour) for API access
- ✅ **Refresh Tokens** - Long-lived tokens (7 days) for token renewal
- ✅ **Token Refresh** - Renew access tokens without re-authentication
- ✅ **User Logout** - Secure logout with security context clearing
- ✅ **Proper HTTP Status Codes** - 200, 201, 401, 403, 409, etc.
- ✅ **Structured JSON Responses** - Consistent API response format
- ✅ **Global Exception Handling** - Centralized error management
- ✅ **BCrypt Password Encryption** - Secure password storage
- ✅ **Reusable Architecture** - Easy to integrate into other projects

## 🚀 Quick Start

### Prerequisites

- Java 17+
- Maven 3.6+
- PostgreSQL (or any JPA-compatible database)

### 1. Clone the Repository

```bash
git clone https://github.com/garvsharmxa/spring-security.git
cd spring-security
```

### 2. Configure Database

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/your_database
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 3. Set JWT Secret

```bash
export JWT_SECRET="your-secure-secret-key-min-32-characters"
```

### 4. Build and Run

```bash
./mvnw clean package
java -jar target/SpringSecEx-0.0.1-SNAPSHOT.jar
```

### 5. Test the API

```bash
# Register a user
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"testpass123"}'

# Login
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"testpass123"}'
```

## 📚 Documentation

- **[AUTH_MODULE_USAGE.md](AUTH_MODULE_USAGE.md)** - Complete API endpoint documentation
- **[INTEGRATION_GUIDE.md](INTEGRATION_GUIDE.md)** - How to integrate into your project
- **[API_EXAMPLES.md](API_EXAMPLES.md)** - Real API response examples
- **[test-api.sh](test-api.sh)** - Automated API testing script

## 🔐 API Endpoints

### Authentication Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/auth/register` | Register new user | No |
| POST | `/auth/login` | Login and get tokens | No |
| POST | `/auth/refresh` | Refresh access token | No |
| POST | `/auth/logout` | Logout user | Yes |

### Legacy Endpoints (Backward Compatible)

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/register` | Register new user | No |
| POST | `/login` | Login and get tokens | No |

### Example Response Structure

```json
{
  "success": true,
  "message": "Operation successful",
  "data": {
    "accessToken": "eyJhbGc...",
    "refreshToken": "eyJhbGc...",
    "username": "john_doe",
    "tokenType": "Bearer",
    "expiresIn": 3600000
  },
  "statusCode": 200
}
```

## 🏗️ Architecture

### Package Structure

```
com.garv.SpringSecEx/
├── Conig/                  # Security configuration
│   └── SecurityConfig.java
├── Controller/             # REST controllers
│   ├── AuthController.java     (Main authentication endpoints)
│   ├── UserController.java     (Legacy endpoints)
│   └── HelloController.java
├── Services/               # Business logic
│   ├── UserService.java
│   └── MyUserDetailsService.java
├── Repository/             # Data access
│   └── UserRepository.java
├── Entity/                 # Domain models
│   ├── Users.java
│   └── UserPrincipal.java
├── Utlity/                 # JWT utilities
│   ├── JwtUtil.java
│   └── JwtFilter.java
├── dto/                    # Data Transfer Objects
│   ├── ApiResponse.java
│   ├── AuthResponse.java
│   ├── LoginRequest.java
│   ├── RegisterRequest.java
│   ├── RefreshTokenRequest.java
│   └── UserResponse.java
└── exception/              # Exception handling
    ├── GlobalExceptionHandler.java
    ├── UserAlreadyExistsException.java
    ├── InvalidCredentialsException.java
    └── InvalidTokenException.java
```

## 🔧 Configuration

### Token Expiration

Modify in `JwtUtil.java`:

```java
private final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hour for access token
private final long REFRESH_TOKEN_EXPIRATION_TIME = 7 * 24 * 60 * 60 * 1000L; // 7 days for refresh token
```

### Security Settings

Configure in `SecurityConfig.java`:

```java
.requestMatchers("/auth/register", "/auth/login", "/auth/refresh").permitAll()
.anyRequest().authenticated()
```

## 🧪 Testing

### Automated Testing

Run the included test script:

```bash
chmod +x test-api.sh
./test-api.sh
```

This will test:
- User registration
- User login
- Token refresh
- Protected endpoint access
- Logout
- Error handling (invalid credentials, duplicate users, etc.)

### Manual Testing with curl

See [API_EXAMPLES.md](API_EXAMPLES.md) for detailed examples.

## 🔌 Integration into Your Project

### Quick Copy Method

1. Copy the following packages to your project:
   - `dto/`
   - `exception/`
   - `Utlity/`
   - `Services/`
   - `Controller/AuthController.java`
   - `Entity/`
   - `Repository/`
   - `Conig/SecurityConfig.java`

2. Update package names to match your project

3. Add required dependencies (see `pom.xml`)

4. Configure `application.properties`

For detailed integration instructions, see [INTEGRATION_GUIDE.md](INTEGRATION_GUIDE.md).

## 🔒 Security Features

- ✅ BCrypt password hashing (strength 12)
- ✅ JWT token validation
- ✅ Token expiration handling
- ✅ Stateless session management
- ✅ CSRF protection disabled (for stateless API)
- ✅ Global exception handling
- ✅ Input validation
- ✅ Secure token storage recommendations

### Security Best Practices Implemented

1. **Password Encryption**: BCrypt with salt rounds
2. **Token Security**: HMAC-SHA256 signature
3. **Token Expiration**: Short-lived access tokens
4. **Refresh Token Rotation**: Available through refresh endpoint
5. **No Password Exposure**: Passwords never returned in responses

## 📝 HTTP Status Codes

| Status Code | Description | Use Case |
|-------------|-------------|----------|
| 200 OK | Success | Login, refresh, logout success |
| 201 Created | Resource created | User registration success |
| 401 Unauthorized | Authentication failed | Invalid credentials, expired token |
| 403 Forbidden | Access denied | Missing or invalid token |
| 409 Conflict | Resource conflict | Username already exists |
| 500 Internal Server Error | Server error | Unexpected errors |

## 🛠️ Technologies Used

- **Spring Boot 3.5.7** - Application framework
- **Spring Security** - Authentication and authorization
- **Spring Data JPA** - Database operations
- **PostgreSQL** - Database
- **JWT (jjwt 0.13.0)** - Token generation and validation
- **BCrypt** - Password encryption
- **Maven** - Build tool
- **Java 17** - Programming language

## 📦 Dependencies

Key dependencies in `pom.xml`:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.13.0</version>
</dependency>
```

See full `pom.xml` for complete dependency list.

## 🤝 Contributing

This is a learning/example project. Feel free to fork and modify for your needs.

## 📄 License

This project is available for use in your projects.

## 🙋 Support

For questions or issues:
- Review the documentation files
- Check [API_EXAMPLES.md](API_EXAMPLES.md) for usage examples
- See [INTEGRATION_GUIDE.md](INTEGRATION_GUIDE.md) for integration help

## ⚡ Key Improvements Over Basic JWT

1. **Structured Responses**: Consistent API response format
2. **Proper Status Codes**: HTTP semantics followed correctly
3. **Refresh Tokens**: Secure token renewal mechanism
4. **Exception Handling**: Centralized error management
5. **Reusability**: Clean architecture, easy to extract
6. **Documentation**: Comprehensive guides and examples
7. **Testing**: Included test script
8. **Production Ready**: Security best practices implemented

## 🎯 Use Cases

Perfect for:
- RESTful API backends
- Microservices authentication
- Mobile app backends
- Single Page Applications (SPA)
- Learning JWT authentication
- Starter template for new projects

## 🚦 Next Steps

After setting up:

1. **Customize User Entity**: Add email, phone, roles
2. **Add Role-Based Access**: Implement ADMIN, USER roles
3. **Token Blacklist**: For complete logout functionality
4. **Rate Limiting**: Prevent brute force attacks
5. **Password Policies**: Add validation rules
6. **Email Verification**: Verify user emails
7. **Password Reset**: Forgot password flow
8. **2FA**: Two-factor authentication

See [INTEGRATION_GUIDE.md](INTEGRATION_GUIDE.md) for implementation examples.
