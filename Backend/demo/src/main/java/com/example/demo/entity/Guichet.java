package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Guichet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private boolean ouvert;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "operateur_id")
    @JsonIgnoreProperties({"guichet", "tickets", "rendezVousList", "password"})
    private User operateur;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "agence_id")
    @JsonBackReference(value = "agence-guichets")
    private Agence agence;

    @OneToMany(mappedBy = "guichet", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore // évite boucle infinie
    private List<Ticket> tickets;

    // Getters & Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public boolean isOuvert() { return ouvert; }
    public void setOuvert(boolean ouvert) { this.ouvert = ouvert; }

    public User getOperateur() { return operateur; }
    public void setOperateur(User operateur) { this.operateur = operateur; }

    public Agence getAgence() { return agence; }
    public void setAgence(Agence agence) { this.agence = agence; }

    public List<Ticket> getTickets() { return tickets; }
    public void setTickets(List<Ticket> tickets) { this.tickets = tickets; }
}
