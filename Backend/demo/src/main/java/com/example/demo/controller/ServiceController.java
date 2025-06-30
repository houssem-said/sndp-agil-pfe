package com.example.demo.controller;

import com.example.demo.entity.ServiceEntity;
import com.example.demo.service.ServiceEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour gérer les services proposés par les agences.
 * Permet la création, consultation, modification et suppression de services.
 */
@RestController
@RequestMapping("/api/services")
// Autorise les requêtes cross-origin depuis le frontend local (Vite, React)
@CrossOrigin(origins = "http://localhost:5173")
public class ServiceController {

    @Autowired
    private ServiceEntityService serviceEntityService;

    /**
     * Récupère la liste complète des services.
     *
     * @return liste de tous les services en base
     */
    @GetMapping
    public List<ServiceEntity> getAllServices() {
        return serviceEntityService.findAll();
    }

    /**
     * Crée un nouveau service.
     *
     * @param service objet service envoyé dans la requête
     * @return service créé avec son identifiant
     */
    @PostMapping
    public ResponseEntity<ServiceEntity> create(@RequestBody ServiceEntity service) {
        return ResponseEntity.ok(serviceEntityService.save(service));
    }

    /**
     * Récupère un service par son identifiant.
     *
     * @param id identifiant du service
     * @return service trouvé ou 404 si non trouvé
     */
    @GetMapping("/{id}")
    public ResponseEntity<ServiceEntity> getById(@PathVariable Long id) {
        ServiceEntity s = serviceEntityService.getById(id);
        return s != null ? ResponseEntity.ok(s) : ResponseEntity.notFound().build();
    }

    /**
     * Met à jour un service existant.
     * Met à jour uniquement le nom du service.
     *
     * @param id identifiant du service à modifier
     * @param service données mises à jour
     * @return service modifié ou 404 si non trouvé
     */
    @PutMapping("/{id}")
    public ResponseEntity<ServiceEntity> update(@PathVariable Long id, @RequestBody ServiceEntity service) {
        ServiceEntity existing = serviceEntityService.getById(id);
        if (existing == null) return ResponseEntity.notFound().build();

        existing.setNom(service.getNom());
        return ResponseEntity.ok(serviceEntityService.save(existing));
    }

    /**
     * Supprime un service par son identifiant.
     *
     * @param id identifiant du service à supprimer
     * @return 204 No Content si supprimé, 404 si non trouvé
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (serviceEntityService.getById(id) == null) return ResponseEntity.notFound().build();

        serviceEntityService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
