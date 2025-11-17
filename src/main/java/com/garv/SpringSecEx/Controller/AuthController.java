package com.garv.SpringSecEx.Controller;

import com.garv.SpringSecEx.Services.UserService;
import com.garv.SpringSecEx.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Authentication Controller - Handles user registration, login, token refresh, and logout
 * This controller provides RESTful endpoints for authentication with proper HTTP status codes
 * and response structures, making it reusable across different projects.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    /**
     * Register a new user
     * @param registerRequest User registration details (username, password)
     * @return ApiResponse with registered user details and HTTP 201 CREATED
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> register(@RequestBody RegisterRequest registerRequest) {
        UserResponse userResponse = userService.register(registerRequest);
        
        ApiResponse<UserResponse> response = ApiResponse.success(
                userResponse,
                "User registered successfully",
                HttpStatus.CREATED.value()
        );
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Login user and receive access and refresh tokens
     * @param loginRequest User login credentials (username, password)
     * @return ApiResponse with authentication tokens and HTTP 200 OK
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody LoginRequest loginRequest) {
        AuthResponse authResponse = userService.login(loginRequest);
        
        ApiResponse<AuthResponse> response = ApiResponse.success(
                authResponse,
                "Login successful",
                HttpStatus.OK.value()
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * Refresh access token using refresh token
     * @param refreshTokenRequest Refresh token request containing the refresh token
     * @return ApiResponse with new access token and HTTP 200 OK
     */
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        AuthResponse authResponse = userService.refreshAccessToken(refreshTokenRequest.getRefreshToken());
        
        ApiResponse<AuthResponse> response = ApiResponse.success(
                authResponse,
                "Token refreshed successfully",
                HttpStatus.OK.value()
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * Logout user - clears security context
     * Note: In a stateless JWT setup, the client should also discard tokens
     * @return ApiResponse with logout confirmation and HTTP 200 OK
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Object>> logout() {
        userService.logout();
        
        ApiResponse<Object> response = ApiResponse.success(
                null,
                "Logged out successfully",
                HttpStatus.OK.value()
        );
        
        return ResponseEntity.ok(response);
    }
}
