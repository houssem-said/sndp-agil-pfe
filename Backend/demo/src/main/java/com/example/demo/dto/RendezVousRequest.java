package com.example.demo.dto;

/**
 * DTO (Data Transfer Object) pour la création d'un rendez-vous.
 * Cette classe permet de transporter les données nécessaires à la création
 * d’un rendez-vous entre le frontend et le backend sans exposer directement les entités.
 */
public class RendezVousRequest {

    private Long userId;     // ID de l'utilisateur qui prend le rendez-vous
    private Long agenceId;   // ID de l'agence choisie
    private Long serviceId;  // ID du service choisi
    private String date;     // Date du rendez-vous (format : "yyyy-MM-dd")
    private String heure;    // Heure du rendez-vous (ex : "10:30")

    // Getter pour l'ID utilisateur
    public Long getUserId() {
        return userId;
    }

    // Setter pour l'ID utilisateur
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    // Getter pour l'heure du rendez-vous
    public String getHeure() {
        return heure;
    }

    // Setter pour l'heure du rendez-vous
    public void setHeure(String heure) {
        this.heure = heure;
    }

    // Getter pour la date du rendez-vous
    public String getDate() {
        return date;
    }

    // Setter pour la date du rendez-vous
    public void setDate(String date) {
        this.date = date;
    }

    // Getter pour l'ID de l'agence
    public Long getAgenceId() {
        return agenceId;
    }

    // Setter pour l'ID de l'agence
    public void setAgenceId(Long agenceId) {
        this.agenceId = agenceId;
    }

    // Getter pour l'ID du service
    public Long getServiceId() {
        return serviceId;
    }

    // Setter pour l'ID du service
    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }
}
