package com.sndp.agil.backend.dto;

import com.sndp.agil.backend.model.Ticket;
import java.time.format.DateTimeFormatter;

/**
 * DTO pour exposer les tickets au frontend sans dévoiler l’entité JPA complète.
 */
public class TicketDTO {
    private Long id;
    private String number;
    private String status;
    private String serviceName;
    private String clientName;
    private String createdAt; // formaté en "yyyy-MM-dd HH:mm"
    private boolean urgent;

    public TicketDTO() {
    }

    // Getters et setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isUrgent() {
        return urgent;
    }

    public void setUrgent(boolean urgent) {
        this.urgent = urgent;
    }

    /**
     * Convertit une entité Ticket en TicketDTO.
     */
    public static TicketDTO fromEntity(Ticket t) {
        TicketDTO dto = new TicketDTO();
        dto.setId(t.getId());
        dto.setNumber(t.getNumero());
        dto.setStatus(t.getStatut().name());
        dto.setUrgent(t.isUrgent());

        // Nom du service associé au guichet du ticket
        if (t.getGuichet() != null && t.getGuichet().getService() != null) {
            dto.setServiceName(t.getGuichet().getService().getNom());
        } else {
            dto.setServiceName(null);
        }

        // Nom du client (utilisateur) qui a pris le ticket
        if (t.getUser() != null) {
            dto.setClientName(t.getUser().getNom());
        } else {
            dto.setClientName(null);
        }

        // Formatage de la date de création : "yyyy-MM-dd HH:mm"
        if (t.getDateCreation() != null) {
            dto.setCreatedAt(
                    t.getDateCreation()
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
            );
        } else {
            dto.setCreatedAt(null);
        }

        return dto;
    }
}
