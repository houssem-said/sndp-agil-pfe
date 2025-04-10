package com.sndp.agil.backend.exception;

import java.time.LocalDateTime;

public class ConflitRendezVousException extends RuntimeException {
    private final LocalDateTime dateConflit;
    private final Long serviceId;

    public ConflitRendezVousException(LocalDateTime dateConflit, Long serviceId) {
        super(String.format("Conflit de rendez-vous le %s pour le service %d", dateConflit, serviceId));
        this.dateConflit = dateConflit;
        this.serviceId = serviceId;
    }

    public LocalDateTime getDateConflit() {
        return dateConflit;
    }

    public Long getServiceId() {
        return serviceId;
    }
}