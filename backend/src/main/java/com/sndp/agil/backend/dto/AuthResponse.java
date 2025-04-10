package com.sndp.agil.backend.dto;

public record AuthResponse(
        String token,
        String tokenType,
        String roles
) {
    public AuthResponse(String token, String tokenType, String roles) {
        this.token = token;
        this.tokenType = tokenType;
        this.roles = roles;
    }
}