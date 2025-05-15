package com.sndp.agil.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "rendez_vous")
public class RendezVous {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dateHeure;

    private String reference;

    private String motif;

    @Enumerated(EnumType.STRING)
    private StatutRendezVous statut = StatutRendezVous.CONFIRME;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private Utilisateur utilisateur;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;

    @PrePersist
    public void generateReference() {
        this.reference = "RDV-" + LocalDateTime.now().getYear() + "-" + LocalDateTime.now().getMonth() + "-" + LocalDateTime.now().getYear() + "-" + String.format("%03d", this.id);
    }
}