package com.sndp.agil.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
public class ServiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    private String description;

    private Integer tempsMoyenMinutes;

    @OneToMany(mappedBy = "service")
    private List<Guichet> guichets;

    @OneToMany(mappedBy = "service")
    private List<RendezVous> rendezVous;
}