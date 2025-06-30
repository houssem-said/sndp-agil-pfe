package com.example.demo.dto;

public class FileAttenteDTO {
    private String guichetNom;
    private int count;       // nombre de personnes avant le client
    private int tempsEstime; // en minutes

    // Constructeur
    public FileAttenteDTO(String guichetNom, int count, int tempsEstime) {
        this.guichetNom = guichetNom;
        this.count = count;
        this.tempsEstime = tempsEstime;
    }

    public FileAttenteDTO() {}

    // Getters et setters
    public String getGuichetNom() { return guichetNom; }
    public void setGuichetNom(String guichetNom) { this.guichetNom = guichetNom; }

    public int getCount() { return count; }
    public void setCount(int count) { this.count = count; }

    public int getTempsEstime() { return tempsEstime; }
    public void setTempsEstime(int tempsEstime) { this.tempsEstime = tempsEstime; }
}
