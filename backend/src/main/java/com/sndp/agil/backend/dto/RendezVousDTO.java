package com.sndp.agil.backend.dto;

import com.sndp.agil.backend.model.RendezVous;
import java.time.format.DateTimeFormatter;

/**
 * DTO pour exposer les rendez-vous au frontend sans dévoiler l’entité JPA complète.
 */
public class RendezVousDTO {
    private Long id;
    private String date;        // "yyyy-MM-dd"
    private String heure;       // "HH:mm"
    private String status;
    private String clientNom;
    private String serviceName;
    private String motif;

    public RendezVousDTO() {
    }

    // Getters et setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHeure() {
        return heure;
    }

    public void setHeure(String heure) {
        this.heure = heure;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getClientNom() {
        return clientNom;
    }

    public void setClientNom(String clientNom) {
        this.clientNom = clientNom;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    /**
     * Convertit une entité RendezVous en RendezVousDTO.
     */
    public static RendezVousDTO fromEntity(RendezVous r) {
        RendezVousDTO dto = new RendezVousDTO();
        dto.setId(r.getId());
        if (r.getDateHeure() != null) {
            dto.setDate(r.getDateHeure().toLocalDate().toString());
            dto.setHeure(r.getDateHeure()
                    .toLocalTime()
                    .format(DateTimeFormatter.ofPattern("HH:mm")));
        }
        dto.setStatus(r.getStatut().name());
        if (r.getUser() != null) {
            dto.setClientNom(r.getUser().getNom());
        }
        if (r.getService() != null) {
            dto.setServiceName(r.getService().getNom());
        }
        dto.setMotif(r.getMotif());
        return dto;
    }
}
