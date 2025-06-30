package com.example.demo.dto;

/**
 * DTO (Data Transfer Object) utilisé pour la mise à jour du profil d'un administrateur.
 * Ce modèle permet de transférer les données de manière structurée entre le frontend et le backend,
 * sans exposer directement l'entité User.
 */
public class AdminProfileDto {
    private Long id;               // Identifiant de l'administrateur
    private String nomComplet;     // Nom complet de l'administrateur
    private String email;          // Adresse email de l'administrateur
    private String telephone;      // Numéro de téléphone
    private String motDePasse;     // Mot de passe (optionnel, si modification souhaitée)

    // Getters et setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomComplet() {
        return nomComplet;
    }

    public void setNomComplet(String nomComplet) {
        this.nomComplet = nomComplet;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }
}
