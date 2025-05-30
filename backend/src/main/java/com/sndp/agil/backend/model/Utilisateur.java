package com.sndp.agil.backend.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Data
@Entity
@Table(name = "utilisateur")
@Getter
public class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String motDePasse;

    @Enumerated(EnumType.STRING)
    private RoleUtilisateur role;

    private LocalDateTime dateCreation = LocalDateTime.now();

    private boolean active = false;  // Par défaut, l'utilisateur est inactif

    public Utilisateur() {}

    // Constructeur avec paramètres
    public Utilisateur(String nom, String email, String motDePasse, RoleUtilisateur role) {
        this.nom = nom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.role = role;
    }

    public String getUsername() {
        return this.email;
    }

    // Méthode pour activer l'utilisateur
    public void setActive(boolean active) {
        this.active = active;
    }

    // Méthode pour vérifier si l'utilisateur est actif
    public boolean isActive() {
        return active;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @JsonManagedReference  // Ajout de la gestion de la sérialisation côté Utilisateur
    @OneToMany(mappedBy = "utilisateur")
    private List<Ticket> tickets;

    @JsonManagedReference  // Ajout de la gestion de la sérialisation côté Utilisateur
    @OneToMany(mappedBy = "utilisateur")
    private List<RendezVous> rendezVous;
}
