package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numero;
    private boolean appele;
    private LocalDateTime dateCreation;
    private LocalDateTime dateTicket;

    @ManyToOne
    @JsonIgnoreProperties({"tickets", "guichet", "rendezVousList", "password"})
    @JsonBackReference("user-tickets")
    private User user;

    @ManyToOne
    @JsonIgnoreProperties({"tickets", "operateur", "agence"})
    private Guichet guichet;

    @ManyToOne
    @JsonIgnoreProperties({"guichets", "services"})
    private Agence agence;

    // Getters & Setters

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getNumero() { return numero; }

    public void setNumero(String numero) { this.numero = numero; }

    public boolean isAppele() { return appele; }

    public void setAppele(boolean appele) { this.appele = appele; }

    public LocalDateTime getDateCreation() { return dateCreation; }

    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }

    public LocalDateTime getDateTicket() { return dateTicket; }

    public void setDateTicket(LocalDateTime dateTicket) { this.dateTicket = dateTicket; }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

    public Guichet getGuichet() { return guichet; }

    public void setGuichet(Guichet guichet) { this.guichet = guichet; }

    public Agence getAgence() { return agence; }

    public void setAgence(Agence agence) { this.agence = agence; }
}
