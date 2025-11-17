# Integration Guide - Using JWT Authentication Module in Your Project

## Quick Start Integration

This guide shows you how to integrate this JWT authentication module into your own Spring Boot project.

## Method 1: Copy Files Directly (Recommended for Learning)

### Step 1: Copy Package Structure
Copy these directories to your project's `src/main/java/your/package/`:

```
dto/                    # All request/response DTOs
exception/              # Custom exceptions and global handler
Utlity/                 # JWT utilities (JwtUtil, JwtFilter)
Services/               # UserService, MyUserDetailsService
Controller/             # AuthController (and optionally UserController)
Entity/                 # Users, UserPrincipal
Repository/             # UserRepository
Conig/                  # SecurityConfig
```

### Step 2: Update Package Names
Replace all occurrences of `com.garv.SpringSecEx` with your own package name:

```bash
# Linux/Mac
find . -type f -name "*.java" -exec sed -i 's/com.garv.SpringSecEx/com.yourcompany.yourapp/g' {} +

# Windows PowerShell
Get-ChildItem -Recurse -Filter *.java | ForEach-Object { 
    (Get-Content $_.FullName) -replace 'com.garv.SpringSecEx', 'com.yourcompany.yourapp' | 
    Set-Content $_.FullName 
}
```

### Step 3: Add Dependencies to pom.xml

```xml
<dependencies>
    <!-- Spring Boot Starter Web -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- Spring Boot Starter Security -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>

    <!-- Spring Boot Starter Data JPA -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>

    <!-- PostgreSQL Driver (or your preferred database) -->
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
        <scope>runtime</scope>
    </dependency>

    <!-- JWT Dependencies -->
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
</dependencies>
```

### Step 4: Configure application.properties

```properties
# Application Name
spring.application.name=YourApp

# Server Configuration
server.port=8080

# Database Configuration (PostgreSQL example)
spring.datasource.url=jdbc:postgresql://localhost:5432/your_database
spring.datasource.username=your_username
spring.datasource.password=your_password

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect

# JWT Configuration
jwt.secret=${JWT_SECRET:your-very-secure-secret-key-minimum-32-characters-long}
```

### Step 5: Set Environment Variable (Recommended)

**Linux/Mac:**
```bash
export JWT_SECRET="your-production-secret-key-here-min-32-chars"
```

**Windows:**
```cmd
set JWT_SECRET=your-production-secret-key-here-min-32-chars
```

**Docker:**
```dockerfile
ENV JWT_SECRET=your-production-secret-key-here-min-32-chars
```

## Method 2: Create as a Separate Module (Recommended for Production)

### Step 1: Create Authentication Module

Create a new Maven module `auth-module`:

```xml
<!-- auth-module/pom.xml -->
<project>
    <modelVersion>4.0.0</modelVersion>
    
    <groupId>com.yourcompany</groupId>
    <artifactId>auth-module</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging>
    
    <dependencies>
        <!-- Spring Boot dependencies -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <!-- JWT dependencies -->
        <!-- ... -->
    </dependencies>
</project>
```

### Step 2: Use in Your Project

```xml
<!-- your-app/pom.xml -->
<dependency>
    <groupId>com.yourcompany</groupId>
    <artifactId>auth-module</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Step 3: Enable Component Scanning

```java
@SpringBootApplication
@ComponentScan(basePackages = {
    "com.yourcompany.yourapp",
    "com.yourcompany.authmodule"  // Include auth module package
})
public class YourApplication {
    public static void main(String[] args) {
        SpringApplication.run(YourApplication.class, args);
    }
}
```

## Customization Examples

### Adding Email Field to User Registration

**1. Update Users Entity:**
```java
@Entity
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String username;
    private String password;
    private String email;  // Add this
    
    // Getters and setters
}
```

**2. Update RegisterRequest:**
```java
public class RegisterRequest {
    private String username;
    private String password;
    private String email;  // Add this
    
    // Getters and setters
}
```

**3. Update UserService.register():**
```java
public UserResponse register(RegisterRequest registerRequest) {
    // ... existing validation ...
    
    Users user = new Users();
    user.setUsername(registerRequest.getUsername());
    user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
    user.setEmail(registerRequest.getEmail());  // Add this
    
    // ... rest of the method
}
```

### Adding User Roles (ADMIN, USER, etc.)

**1. Create Role Entity:**
```java
@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;  // ROLE_USER, ROLE_ADMIN
    
    // Getters and setters
}
```

**2. Update Users Entity:**
```java
@Entity
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String username;
    private String password;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();
    
    // Getters and setters
}
```

**3. Update UserPrincipal:**
```java
@Override
public Collection<? extends GrantedAuthority> getAuthorities() {
    return user.getRoles().stream()
        .map(role -> new SimpleGrantedAuthority(role.getName()))
        .collect(Collectors.toList());
}
```

**4. Use in Controllers:**
```java
@PreAuthorize("hasRole('ADMIN')")
@GetMapping("/admin/dashboard")
public ResponseEntity<?> adminDashboard() {
    // Only accessible by ADMIN role
}

@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
@GetMapping("/user/profile")
public ResponseEntity<?> userProfile() {
    // Accessible by USER or ADMIN role
}
```

**5. Enable Method Security:**
```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity  // Add this
public class SecurityConfig {
    // ... existing configuration
}
```

### Adding Token Blacklist for Logout

**1. Create Token Blacklist Entity:**
```java
@Entity
public class BlacklistedToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true)
    private String token;
    
    private LocalDateTime blacklistedAt;
    
    // Getters and setters
}
```

**2. Create Repository:**
```java
public interface BlacklistedTokenRepository extends JpaRepository<BlacklistedToken, Long> {
    boolean existsByToken(String token);
}
```

**3. Update JwtFilter:**
```java
@Override
protected void doFilterInternal(HttpServletRequest request,
                                HttpServletResponse response,
                                FilterChain filterChain) {
    // ... existing code ...
    
    // Check if token is blacklisted
    if (jwtToken != null && blacklistedTokenRepository.existsByToken(jwtToken)) {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token has been revoked");
        return;
    }
    
    // ... rest of the method
}
```

**4. Update UserService.logout():**
```java
public void logout(String token) {
    BlacklistedToken blacklistedToken = new BlacklistedToken();
    blacklistedToken.setToken(token);
    blacklistedToken.setBlacklistedAt(LocalDateTime.now());
    blacklistedTokenRepository.save(blacklistedToken);
    
    SecurityContextHolder.clearContext();
}
```

## Testing Your Integration

### Using curl:

```bash
# 1. Register
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"john","password":"pass123"}'

# 2. Login
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"john","password":"pass123"}'

# 3. Use the access token from login response
curl -X GET http://localhost:8080/api/protected \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"

# 4. Refresh token
curl -X POST http://localhost:8080/auth/refresh \
  -H "Content-Type: application/json" \
  -d '{"refreshToken":"YOUR_REFRESH_TOKEN"}'

# 5. Logout
curl -X POST http://localhost:8080/auth/logout \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

### Using JavaScript (Frontend):

```javascript
// Register
const register = async (username, password) => {
  const response = await fetch('http://localhost:8080/auth/register', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ username, password })
  });
  return await response.json();
};

// Login
const login = async (username, password) => {
  const response = await fetch('http://localhost:8080/auth/login', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ username, password })
  });
  const data = await response.json();
  
  // Store tokens
  localStorage.setItem('accessToken', data.data.accessToken);
  localStorage.setItem('refreshToken', data.data.refreshToken);
  
  return data;
};

// Call protected API
const callProtectedAPI = async () => {
  const token = localStorage.getItem('accessToken');
  const response = await fetch('http://localhost:8080/api/protected', {
    headers: { 'Authorization': `Bearer ${token}` }
  });
  return await response.json();
};

// Refresh token
const refreshToken = async () => {
  const refreshToken = localStorage.getItem('refreshToken');
  const response = await fetch('http://localhost:8080/auth/refresh', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ refreshToken })
  });
  const data = await response.json();
  
  // Update access token
  localStorage.setItem('accessToken', data.data.accessToken);
  
  return data;
};

// Logout
const logout = async () => {
  const token = localStorage.getItem('accessToken');
  await fetch('http://localhost:8080/auth/logout', {
    method: 'POST',
    headers: { 'Authorization': `Bearer ${token}` }
  });
  
  // Clear tokens
  localStorage.removeItem('accessToken');
  localStorage.removeItem('refreshToken');
};
```

## Common Issues and Solutions

### Issue: "No qualifying bean of type 'org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder'"

**Solution:** Add `@Bean` for BCryptPasswordEncoder in SecurityConfig:

```java
@Bean
public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(12);
}
```

### Issue: Database connection errors

**Solution:** Verify your database is running and credentials are correct:

```bash
# Test PostgreSQL connection
psql -h localhost -U your_username -d your_database
```

### Issue: CORS errors from frontend

**Solution:** Add CORS configuration:

```java
@Bean
public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")  // Your frontend URL
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true);
        }
    };
}
```

## Production Checklist

- [ ] Use strong, unique JWT secret (min 32 characters)
- [ ] Store JWT secret in environment variable, not in code
- [ ] Use HTTPS in production
- [ ] Implement rate limiting on auth endpoints
- [ ] Add password strength validation
- [ ] Implement account lockout after failed attempts
- [ ] Use database connection pooling
- [ ] Add logging and monitoring
- [ ] Implement token refresh rotation
- [ ] Add CSRF protection for non-API endpoints
- [ ] Configure proper CORS settings
- [ ] Use prepared statements (already done via JPA)
- [ ] Regular security audits

## Support

For more details, refer to:
- `AUTH_MODULE_USAGE.md` - API endpoint documentation
- `test-api.sh` - Example test script

## License

Free to use in your projects.
