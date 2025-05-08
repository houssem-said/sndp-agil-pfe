package com.sndp.agil.backend.dto;

public record AuthResponse(
        String token,
        String tokenType,
        String roles
) {}