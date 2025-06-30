package com.example.demo.controller;

import com.example.demo.entity.Agence;
import com.example.demo.repository.AgenceRepository;
import com.example.demo.service.AgenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/agences") // Toutes les routes de ce contrôleur commencent par /api/agences
@CrossOrigin(origins = "http://localhost:5173") // Autorise les requêtes cross-origin depuis ce frontend local
public class AgenceController {

    @Autowired
    private AgenceService agenceService; // Injection du service métier pour gérer les agences

    /**
     * Récupère la liste complète des agences
     * @return liste de toutes les agences
     */
    @GetMapping
    public List<Agence> getAllAgences() {
        return agenceService.findAll();
    }

    /**
     * Crée une nouvelle agence avec les données reçues en JSON
     * @param agence agence à créer
     * @return l'agence créée avec son ID généré
     */
    @PostMapping
    public ResponseEntity<Agence> create(@RequestBody Agence agence) {
        Agence saved = agenceService.save(agence);
        return ResponseEntity.ok(saved);
    }

    /**
     * Récupère une agence spécifique par son ID
     * @param id ID de l'agence recherchée
     * @return l'agence si trouvée, sinon 404 Not Found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Agence> getAgenceById(@PathVariable Long id) {
        Optional<Agence> agenceOpt = agenceService.getById(id);
        return agenceOpt.map(ResponseEntity::ok) // si présente, retourne 200 OK avec agence
                .orElseGet(() -> ResponseEntity.notFound().build()); // sinon 404
    }

    /**
     * Supprime une agence donnée par son ID
     * @param id ID de l'agence à supprimer
     * @return 204 No Content si suppression réussie, 404 si agence non trouvée
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<Agence> agenceOpt = agenceService.getById(id);
        if (agenceOpt.isEmpty()) {
            return ResponseEntity.notFound().build(); // pas trouvée → 404
        }
        agenceService.delete(id); // suppression de l'agence
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    /**
     * Met à jour une agence existante identifiée par son ID
     * @param id ID de l'agence à mettre à jour
     * @param agence objet agence contenant les nouvelles données
     * @return l'agence mise à jour, ou 404 si non trouvée
     */
    @PutMapping("/{id}")
    public ResponseEntity<Agence> updateAgence(@PathVariable Long id, @RequestBody Agence agence) {
        Optional<Agence> existOpt = agenceService.getById(id);
        if (existOpt.isEmpty()) {
            return ResponseEntity.notFound().build(); // agence non trouvée
        }
        Agence exist = existOpt.get();
        // Mise à jour des champs modifiables
        exist.setNom(agence.getNom());
        exist.setAdresse(agence.getAdresse());
        Agence updated = agenceService.save(exist); // sauvegarde des modifications
        return ResponseEntity.ok(updated); // retourne l'agence mise à jour
    }
}
