package com.sndp.agil.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
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
    private User user;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private ServiceEntity service;

    @PrePersist
    public void generateReference() {
        LocalDateTime now = LocalDateTime.now();
        this.reference = "RDV-" + now.getYear() + "-" + now.getMonthValue() + "-" + now.getDayOfMonth() + "-" + now.getHour() + now.getMinute() + now.getSecond();
    }
}
