package com.sndp.agil.backend.dto;

public record LoginRequest(
        String username,
        String password
) {}