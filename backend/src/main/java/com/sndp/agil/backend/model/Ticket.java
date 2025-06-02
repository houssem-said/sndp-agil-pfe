package com.sndp.agil.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "ticket")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 20)
    private String numero;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private StatutTicket statut = StatutTicket.EN_ATTENTE;

    @Column(name = "date_creation", nullable = false, updatable = false)
    private LocalDateTime dateCreation = LocalDateTime.now();

    @Column(name = "date_traitement")
    private LocalDateTime dateTraitement;

    @Column(name = "urgent", nullable = false) // Changé pour matcher la DB
    private boolean urgent = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guichet_id", nullable = false)
    private Guichet guichet;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private User user;

    @PrePersist
    public void generateNumero() {
        if (this.numero == null) {
            this.numero = (this.urgent ? "TU-" : "TN-") +
                    this.dateCreation.toLocalDate().format(java.time.format.DateTimeFormatter.BASIC_ISO_DATE) +
                    "-" + String.format("%04d", this.id);
        }
    }
}