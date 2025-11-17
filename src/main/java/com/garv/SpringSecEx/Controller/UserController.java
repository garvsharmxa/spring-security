package com.garv.SpringSecEx.Controller;

import com.garv.SpringSecEx.Services.UserService;
import com.garv.SpringSecEx.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Legacy User Controller - Maintained for backward compatibility
 * For new implementations, use AuthController at /auth/* endpoints
 */
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * @deprecated Use /auth/register instead
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> registerUser(@RequestBody RegisterRequest registerRequest) {
        UserResponse userResponse = userService.register(registerRequest);
        
        ApiResponse<UserResponse> response = ApiResponse.success(
                userResponse,
                "User registered successfully",
                HttpStatus.CREATED.value()
        );
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * @deprecated Use /auth/login instead
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> loginUser(@RequestBody LoginRequest loginRequest) {
        AuthResponse authResponse = userService.login(loginRequest);
        
        ApiResponse<AuthResponse> response = ApiResponse.success(
                authResponse,
                "Login successful",
                HttpStatus.OK.value()
        );
        
        return ResponseEntity.ok(response);
    }
}
