package com.example.demo.entity;

import jakarta.persistence.*;

/**
 * Représente un service proposé par une agence (ex: Paiement de facture, Retrait, Dépôt...).
 * Cette entité peut être associée ultérieurement à des guichets ou rendez-vous.
 */
@Entity
public class ServiceEntity {

    /**
     * Identifiant unique du service.
     * Généré automatiquement avec une stratégie d'auto-incrément.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nom du service (ex: "Retrait", "Assistance carte", etc.).
     */
    private String nom;

    // ======== Getters et Setters ========

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
