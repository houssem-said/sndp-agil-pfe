package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.List;

/**
 * Représente une agence SNDP.
 * Une agence peut contenir plusieurs guichets.
 */
@Entity
public class Agence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Identifiant unique de l'agence (clé primaire)

    private String nom;     // Nom de l'agence
    private String adresse; // Adresse physique de l'agence

    /**
     * Liste des guichets associés à cette agence.
     * La relation est de type OneToMany (une agence possède plusieurs guichets).
     * - mappedBy = "agence" indique que la relation est gérée par l'attribut 'agence' de l'entité Guichet.
     * - fetch = EAGER signifie que les guichets seront chargés automatiquement avec l'agence.
     * - cascade = ALL permet de propager toutes les opérations JPA (persist, remove, etc.) aux guichets.
     * - @JsonManagedReference est utilisé pour éviter les boucles infinies lors de la sérialisation JSON (liaison avec @JsonBackReference dans Guichet).
     */
    @OneToMany(mappedBy = "agence", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "agence-guichets")
    private List<Guichet> guichets;

    // ======== Getters & Setters ========

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

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public List<Guichet> getGuichets() {
        return guichets;
    }

    public void setGuichets(List<Guichet> guichets) {
        this.guichets = guichets;
    }
}
