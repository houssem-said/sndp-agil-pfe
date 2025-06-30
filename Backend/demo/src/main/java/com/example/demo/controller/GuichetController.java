package com.example.demo.controller;

import com.example.demo.entity.Guichet;
import com.example.demo.entity.User;
import com.example.demo.service.GuichetService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/guichets") // Route principale pour gérer les guichets
public class GuichetController {

    @Autowired
    private GuichetService guichetService;

    @Autowired
    private UserService userService;

    /**
     * Crée un nouveau guichet.
     * Si un opérateur est fourni avec un ID, on l’assigne au guichet via le service.
     * Sinon, on sauvegarde juste le guichet.
     */
    @PostMapping
    public ResponseEntity<Guichet> create(@RequestBody Guichet guichet) {
        if (guichet.getOperateur() != null && guichet.getOperateur().getId() != null) {
            User operateur = userService.getById(guichet.getOperateur().getId());
            if (operateur != null) {
                // Assigner l’opérateur au guichet avant sauvegarde
                Guichet saved = guichetService.assignOperateurToGuichet(null, operateur, guichet);
                return ResponseEntity.ok(saved);
            }
        }
        // Sauvegarde simple si pas d’opérateur ou opérateur introuvable
        Guichet saved = guichetService.save(guichet);
        return ResponseEntity.ok(saved);
    }

    /**
     * Récupère la liste complète des guichets.
     */
    @GetMapping
    public List<Guichet> getAll() {
        return guichetService.findAll();
    }

    /**
     * Récupère un guichet par son ID.
     */
    @GetMapping("/{id}")
    public Guichet getById(@PathVariable Long id) {
        return guichetService.getById(id);
    }

    /**
     * Supprime un guichet identifié par son ID.
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        guichetService.delete(id);
    }

    /**
     * Modifie l'état (ouvert/fermé) d’un guichet.
     * @param ouvert true pour ouvrir, false pour fermer
     */
    @PutMapping("/{id}/etat")
    public ResponseEntity<?> modifierEtatGuichet(@PathVariable Long id, @RequestParam boolean ouvert) {
        Guichet guichet = guichetService.getById(id);
        if (guichet == null) return ResponseEntity.notFound().build();

        guichet.setOuvert(ouvert);
        guichetService.save(guichet);
        return ResponseEntity.ok("Guichet " + (ouvert ? "ouvert" : "fermé") + " avec succès.");
    }

    /**
     * Met à jour un guichet complet (détails et opérateur).
     * Utilise assignOperateurToGuichet si un opérateur est fourni pour gérer l’affectation.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateGuichet(@PathVariable Long id, @RequestBody Guichet guichet) {
        Guichet exist = guichetService.getById(id);
        if (exist == null) return ResponseEntity.notFound().build();

        // Si un opérateur est fourni, on tente de récupérer son entité et assigner
        if (guichet.getOperateur() != null && guichet.getOperateur().getId() != null) {
            User operateur = userService.getById(guichet.getOperateur().getId());
            if (operateur != null) {
                // Assigne opérateur et met à jour le guichet
                Guichet updatedGuichet = guichetService.assignOperateurToGuichet(id, operateur, guichet);
                return ResponseEntity.ok(updatedGuichet);
            } else {
                // Opérateur non trouvé, on désassocie opérateur et met à jour les autres champs
                exist.setOperateur(null);
                exist.setNom(guichet.getNom());
                exist.setOuvert(guichet.isOuvert());
                exist.setAgence(guichet.getAgence());
                Guichet updated = guichetService.save(exist);
                return ResponseEntity.ok(updated);
            }
        } else {
            // Pas d’opérateur fourni, mise à jour standard
            exist.setOperateur(null);
            exist.setNom(guichet.getNom());
            exist.setOuvert(guichet.isOuvert());
            exist.setAgence(guichet.getAgence());
            Guichet updated = guichetService.save(exist);
            return ResponseEntity.ok(updated);
        }
    }

    /**
     * Endpoint optionnel pour assigner un opérateur à un guichet via les IDs dans l'URL.
     * @param userId ID de l'opérateur
     * @param guichetId ID du guichet
     */
    @PutMapping("/assign/{userId}/{guichetId}")
    public ResponseEntity<User> assignGuichetToOperateur(@PathVariable Long userId, @PathVariable Long guichetId) {
        User user = userService.getById(userId);
        if (user == null) return ResponseEntity.notFound().build();

        guichetService.assignOperateurToGuichet(guichetId, user);
        return ResponseEntity.ok(user);
    }
}
