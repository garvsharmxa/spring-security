# API Response Examples

This document shows real examples of API responses from all endpoints.

## Authentication Endpoints

### 1. Register User - Success

**Request:**
```bash
POST /auth/register
Content-Type: application/json

{
  "username": "john_doe",
  "password": "SecurePass123!"
}
```

**Response (HTTP 201 Created):**
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

### 2. Register User - Username Already Exists

**Request:**
```bash
POST /auth/register
Content-Type: application/json

{
  "username": "john_doe",
  "password": "AnotherPass123!"
}
```

**Response (HTTP 409 Conflict):**
```json
{
  "success": false,
  "message": "Username already exists: john_doe",
  "statusCode": 409
}
```

### 3. Login - Success

**Request:**
```bash
POST /auth/login
Content-Type: application/json

{
  "username": "john_doe",
  "password": "SecurePass123!"
}
```

**Response (HTTP 200 OK):**
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJ0eXBlIjoiYWNjZXNzIiwic3ViIjoiam9obl9kb2UiLCJpYXQiOjE3MDA1MDAwMDAsImV4cCI6MTcwMDUwMzYwMH0.abc123...",
    "refreshToken": "eyJhbGciOiJIUzI1NiJ9.eyJ0eXBlIjoicmVmcmVzaCIsInN1YiI6ImpvaG5fZG9lIiwiaWF0IjoxNzAwNTAwMDAwLCJleHAiOjE3MDExMDQ4MDB9.def456...",
    "username": "john_doe",
    "tokenType": "Bearer",
    "expiresIn": 3600000
  },
  "statusCode": 200
}
```

**Token Details:**
- `accessToken`: Valid for 1 hour (3600000 ms)
- `refreshToken`: Valid for 7 days
- `tokenType`: Always "Bearer"
- `expiresIn`: Token expiration time in milliseconds
- `username`: Authenticated user's username

### 4. Login - Invalid Credentials

**Request:**
```bash
POST /auth/login
Content-Type: application/json

{
  "username": "john_doe",
  "password": "WrongPassword"
}
```

**Response (HTTP 401 Unauthorized):**
```json
{
  "success": false,
  "message": "Invalid username or password",
  "statusCode": 401
}
```

### 5. Login - User Not Found

**Request:**
```bash
POST /auth/login
Content-Type: application/json

{
  "username": "nonexistent_user",
  "password": "SomePassword123"
}
```

**Response (HTTP 401 Unauthorized):**
```json
{
  "success": false,
  "message": "Invalid username or password",
  "statusCode": 401
}
```

### 6. Refresh Token - Success

**Request:**
```bash
POST /auth/refresh
Content-Type: application/json

{
  "refreshToken": "eyJhbGciOiJIUzI1NiJ9.eyJ0eXBlIjoicmVmcmVzaCIsInN1YiI6ImpvaG5fZG9lIiwiaWF0IjoxNzAwNTAwMDAwLCJleHAiOjE3MDExMDQ4MDB9.def456..."
}
```

**Response (HTTP 200 OK):**
```json
{
  "success": true,
  "message": "Token refreshed successfully",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJ0eXBlIjoiYWNjZXNzIiwic3ViIjoiam9obl9kb2UiLCJpYXQiOjE3MDA1MDM2MDAsImV4cCI6MTcwMDUwNzIwMH0.xyz789...",
    "refreshToken": "eyJhbGciOiJIUzI1NiJ9.eyJ0eXBlIjoicmVmcmVzaCIsInN1YiI6ImpvaG5fZG9lIiwiaWF0IjoxNzAwNTAwMDAwLCJleHAiOjE3MDExMDQ4MDB9.def456...",
    "username": "john_doe",
    "tokenType": "Bearer",
    "expiresIn": 3600000
  },
  "statusCode": 200
}
```

**Note:** The refresh token remains the same, only the access token is renewed.

### 7. Refresh Token - Invalid or Expired

**Request:**
```bash
POST /auth/refresh
Content-Type: application/json

{
  "refreshToken": "invalid.or.expired.token"
}
```

**Response (HTTP 401 Unauthorized):**
```json
{
  "success": false,
  "message": "Invalid or expired refresh token",
  "statusCode": 401
}
```

### 8. Logout - Success

**Request:**
```bash
POST /auth/logout
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJ0eXBlIjoiYWNjZXNzIiwic3ViIjoiam9obl9kb2UiLCJpYXQiOjE3MDA1MDAwMDAsImV4cCI6MTcwMDUwMzYwMH0.abc123...
```

**Response (HTTP 200 OK):**
```json
{
  "success": true,
  "message": "Logged out successfully",
  "statusCode": 200
}
```

**Note:** After logout, the client should discard both access and refresh tokens. In a stateless JWT setup, the server clears the security context but the token itself remains valid until expiration. For complete token invalidation, implement a token blacklist.

### 9. Logout - Missing Token

**Request:**
```bash
POST /auth/logout
(No Authorization header)
```

**Response (HTTP 403 Forbidden):**
```json
{
  "timestamp": "2024-01-01T12:00:00.000+00:00",
  "status": 403,
  "error": "Forbidden",
  "path": "/auth/logout"
}
```

## Protected Endpoint Examples

### 10. Access Protected Resource - Success

**Request:**
```bash
GET /
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJ0eXBlIjoiYWNjZXNzIiwic3ViIjoiam9obl9kb2UiLCJpYXQiOjE3MDA1MDAwMDAsImV4cCI6MTcwMDUwMzYwMH0.abc123...
```

**Response (HTTP 200 OK):**
```
Hello World abc123session456
```

### 11. Access Protected Resource - No Token

**Request:**
```bash
GET /
(No Authorization header)
```

**Response (HTTP 403 Forbidden):**
```json
{
  "timestamp": "2024-01-01T12:00:00.000+00:00",
  "status": 403,
  "error": "Forbidden",
  "path": "/"
}
```

### 12. Access Protected Resource - Expired Token

**Request:**
```bash
GET /
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.expired.token
```

**Response (HTTP 403 Forbidden):**
```json
{
  "timestamp": "2024-01-01T12:00:00.000+00:00",
  "status": 403,
  "error": "Forbidden",
  "path": "/"
}
```

### 13. Access Protected Resource - Invalid Token

**Request:**
```bash
GET /
Authorization: Bearer invalid-token-format
```

**Response (HTTP 403 Forbidden):**
```json
{
  "timestamp": "2024-01-01T12:00:00.000+00:00",
  "status": 403,
  "error": "Forbidden",
  "path": "/"
}
```

## Status Code Summary

| Endpoint | Success Code | Error Codes |
|----------|--------------|-------------|
| POST /auth/register | 201 Created | 409 Conflict (user exists), 500 Server Error |
| POST /auth/login | 200 OK | 401 Unauthorized (bad credentials), 404 Not Found (user not found) |
| POST /auth/refresh | 200 OK | 401 Unauthorized (invalid/expired token) |
| POST /auth/logout | 200 OK | 403 Forbidden (no/invalid token) |
| Protected Endpoints | 200 OK (varies) | 403 Forbidden (no/invalid/expired token) |

## Error Response Structure

All error responses follow this consistent structure:

```json
{
  "success": false,
  "message": "Error description here",
  "statusCode": 400
}
```

Or for Spring Security default errors:

```json
{
  "timestamp": "2024-01-01T12:00:00.000+00:00",
  "status": 403,
  "error": "Forbidden",
  "path": "/endpoint"
}
```

## Complete Authentication Flow Example

```bash
# 1. Register a new user
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"alice","password":"AlicePass123!"}'

# Response: {"success":true,"message":"User registered successfully","data":{"id":1,"username":"alice"},"statusCode":201}

# 2. Login to get tokens
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"alice","password":"AlicePass123!"}'

# Response: {"success":true,"message":"Login successful","data":{"accessToken":"eyJ...","refreshToken":"eyJ...","username":"alice","tokenType":"Bearer","expiresIn":3600000},"statusCode":200}

# 3. Access protected resource with access token
curl -X GET http://localhost:8080/ \
  -H "Authorization: Bearer eyJ..."

# Response: Hello World session123

# 4. After 1 hour, access token expires, use refresh token
curl -X POST http://localhost:8080/auth/refresh \
  -H "Content-Type: application/json" \
  -d '{"refreshToken":"eyJ..."}'

# Response: {"success":true,"message":"Token refreshed successfully","data":{"accessToken":"eyJ...","refreshToken":"eyJ...","username":"alice","tokenType":"Bearer","expiresIn":3600000},"statusCode":200}

# 5. Logout
curl -X POST http://localhost:8080/auth/logout \
  -H "Authorization: Bearer eyJ..."

# Response: {"success":true,"message":"Logged out successfully","statusCode":200}
```

## Token Structure

### Access Token Claims:
```json
{
  "type": "access",
  "sub": "john_doe",
  "iat": 1700500000,
  "exp": 1700503600
}
```

### Refresh Token Claims:
```json
{
  "type": "refresh",
  "sub": "john_doe",
  "iat": 1700500000,
  "exp": 1701104800
}
```

**Fields:**
- `type`: Token type (access or refresh)
- `sub`: Subject (username)
- `iat`: Issued at timestamp
- `exp`: Expiration timestamp

## Best Practices for Clients

1. **Store tokens securely:**
   - Use httpOnly cookies for web apps
   - Use secure storage for mobile apps
   - Never store in localStorage if possible (XSS risk)

2. **Handle token expiration:**
   - Check token expiry before API calls
   - Automatically refresh tokens when needed
   - Redirect to login on refresh token expiration

3. **Include proper headers:**
   - Always use `Bearer` prefix in Authorization header
   - Set `Content-Type: application/json` for POST requests

4. **Handle errors gracefully:**
   - Check `success` field in response
   - Display appropriate error messages to users
   - Retry with token refresh on 401 errors

5. **Clear tokens on logout:**
   - Remove all stored tokens
   - Clear any cached user data
   - Redirect to login page
