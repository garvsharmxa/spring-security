package com.garv.SpringSecEx.dto;

public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private String username;
    private String tokenType = "Bearer";
    private Long expiresIn;

    public AuthResponse() {
    }

    public AuthResponse(String accessToken, String refreshToken, String username, Long expiresIn) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.username = username;
        this.expiresIn = expiresIn;
    }

    // Getters and Setters
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }
}
