package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Représente un rendez-vous pris par un utilisateur (client) dans une agence à une date et une heure spécifiques.
 */
@Entity
public class RendezVous {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Identifiant unique du rendez-vous

    private LocalDate date; // Date du rendez-vous
    private LocalTime heure;   // Heure du rendez-vous (ex. "10:30")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "service_id") // nom de la colonne FK dans la table RendezVous
    private ServiceEntity service;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "guichet_id") // nom de la colonne FK dans la table RendezVous
    private Guichet guichet;

    /**
     * L'utilisateur ayant pris le rendez-vous.
     * Relation ManyToOne car un utilisateur peut avoir plusieurs rendez-vous.
     * FetchType.LAZY permet de charger les données utilisateur uniquement si nécessaire.
     * @JsonIgnoreProperties évite les erreurs de sérialisation avec Hibernate.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    /**
     * L’agence dans laquelle le rendez-vous est prévu.
     * Relation ManyToOne car une agence peut avoir plusieurs rendez-vous.
     * FetchType.LAZY évite le chargement automatique de l’agence associée.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    private Agence agence;

    // ======== Getters et Setters ========

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Agence getAgence() {
        return agence;
    }

    public void setAgence(Agence agence) {
        this.agence = agence;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getHeure() {
        return heure;
    }

    public void setHeure(LocalTime heure) {
        this.heure = heure;
    }

    public ServiceEntity getService() {
        return service;
    }

    public void setService(ServiceEntity service) {
        this.service = service;
    }

    public Guichet getGuichet() {
        return guichet;
    }

    public void setGuichet(Guichet guichet) {
        this.guichet = guichet;
    }
}
