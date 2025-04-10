package com.sndp.agil.backend.dto;

import java.time.LocalDateTime;

public record NotificationDTO(
        String entityId,
        String message,
        LocalDateTime timestamp
) {}