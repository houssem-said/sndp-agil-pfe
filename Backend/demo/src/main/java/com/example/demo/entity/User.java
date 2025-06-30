package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;

/**
 * Entité représentant un utilisateur du système.
 *
 * Un utilisateur peut être de rôle : USER (client), OPERATEUR, ou ADMIN.
 * Cette classe gère aussi les relations vers les tickets et le guichet (pour opérateurs).
 */
@Entity
public class User {

    /**
     * Identifiant unique auto-généré de l'utilisateur.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nom complet ou prénom+nom de l'utilisateur.
     */
    private String nom;

    /**
     * Adresse email de l'utilisateur (utilisée pour login).
     */
    private String email;

    /**
     * Mot de passe en clair (à améliorer avec chiffrement en production).
     */
    private String motDePasse;

    /**
     * Numéro de téléphone, optionnel.
     */
    private String telephone;

    /**
     * Rôle de l'utilisateur dans le système : USER, OPERATEUR ou ADMIN.
     */
    private String role;

    /**
     * Relation OneToOne bidirectionnelle vers Guichet.
     *
     * Un opérateur peut être associé à un guichet.
     *
     * `mappedBy = "operateur"` indique que la relation est définie côté Guichet.
     * `fetch = FetchType.LAZY` : chargement différé pour optimiser les performances.
     *
     * `@JsonIgnoreProperties` empêche la sérialisation JSON des champs listés pour éviter
     * des boucles infinies ou exposer des données non nécessaires lors de la conversion JSON.
     */
    @OneToOne(mappedBy = "operateur", fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"operateur", "agence", "tickets"})
    private Guichet guichet;

    /**
     * Relation OneToMany vers Ticket.
     *
     * Un utilisateur (client) peut posséder plusieurs tickets.
     *
     * `mappedBy = "user"` signifie que la relation est définie dans l'entité Ticket via son champ `user`.
     *
     * `@JsonManagedReference("user-tickets")` est le côté parent dans la sérialisation JSON,
     * pour gérer la relation bidirectionnelle avec la classe Ticket annotée en `@JsonBackReference`.
     */
    @OneToMany(mappedBy = "user")
    @JsonManagedReference("user-tickets")
    private List<Ticket> tickets;

    // ======= Getters et Setters =======

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Guichet getGuichet() {
        return guichet;
    }

    public void setGuichet(Guichet guichet) {
        this.guichet = guichet;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }
}
