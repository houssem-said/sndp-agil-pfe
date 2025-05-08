package com.sndp.agil.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Data
@Entity
@Table(name = "utilisateur")
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

    // Constructeur par défaut (requis par JPA)
    public Utilisateur() {}

    // Constructeur avec paramètres
    public Utilisateur(String nom, String email, String motDePasse, RoleUtilisateur role) {
        this.nom = nom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.role = role;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @OneToMany(mappedBy = "utilisateur")
    private List<Ticket> tickets;

    @OneToMany(mappedBy = "utilisateur")
    private List<RendezVous> rendezVous;
}
