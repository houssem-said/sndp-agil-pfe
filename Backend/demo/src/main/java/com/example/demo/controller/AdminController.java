package com.example.demo.controller;

import com.example.demo.dto.AdminProfileDto;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/admin") // Préfixe commun à toutes les routes de ce contrôleur
public class AdminController {

    @Autowired
    private UserRepository userRepo; // Injection du repository pour manipuler les utilisateurs en base

    /**
     * Récupère des métriques basiques sur le nombre d’utilisateurs selon leur rôle
     * @return une map avec le total des utilisateurs, clients, opérateurs et admins
     */
    @GetMapping("/metrics")
    public Map<String, Object> getMetrics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", userRepo.count()); // Nombre total d'utilisateurs
        stats.put("totalClients", userRepo.countByRole("USER")); // Nombre clients
        stats.put("totalOperateurs", userRepo.countByRole("OPERATEUR")); // Nombre opérateurs
        stats.put("totalAdmins", userRepo.countByRole("ADMIN")); // Nombre admins
        return stats;
    }

    /**
     * Récupère la liste complète des utilisateurs
     * @return liste des utilisateurs, ou réponse 204 No Content si vide
     */
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> all = userRepo.findAll();
        if (all.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(all);
    }

    /**
     * Supprime un utilisateur donné par son ID
     * Empêche la suppression de l’administrateur principal par email
     * @param id ID de l’utilisateur à supprimer
     * @return message de succès ou d’erreur
     */
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        Optional<User> optUser = userRepo.findById(id);
        if (optUser.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("message", "Utilisateur introuvable"));
        }

        User u = optUser.get();

        // Protection de l'admin principal : ne peut pas être supprimé
        if ("ADMIN".equals(u.getRole()) && u.getEmail().equalsIgnoreCase("admin@sndp.tn")) {
            return ResponseEntity.status(403).body(Map.of("message", "Impossible de supprimer cet administrateur principal."));
        }

        userRepo.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "Utilisateur supprimé avec succès"));
    }

    /**
     * Modifie le profil d’un administrateur (nom, email, téléphone, mot de passe optionnel)
     * @param dto données du profil à modifier
     * @return message de succès ou d’erreur
     */
    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@RequestBody AdminProfileDto dto) {
        if (dto.getId() == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Id utilisateur requis"));
        }

        Optional<User> optAdmin = userRepo.findById(dto.getId());
        if (optAdmin.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("message", "Admin non trouvé"));
        }

        User admin = optAdmin.get();

        // Mise à jour des champs modifiables
        admin.setNom(dto.getNomComplet());
        admin.setEmail(dto.getEmail());
        admin.setTelephone(dto.getTelephone());

        // Modification du mot de passe uniquement si renseigné et non vide
        if (dto.getMotDePasse() != null && !dto.getMotDePasse().isBlank()) {
            admin.setMotDePasse(dto.getMotDePasse());
        }

        userRepo.save(admin); // Sauvegarde en base
        return ResponseEntity.ok(Map.of("message", "Profil mis à jour avec succès"));
    }

    /**
     * Ajoute un nouvel utilisateur (opérateur, admin ou client)
     * Vérifie la présence des champs obligatoires et unicité de l’email
     * @param user objet utilisateur reçu en JSON
     * @return utilisateur créé ou message d’erreur
     */
    @PostMapping("/users")
    public ResponseEntity<?> addUser(@RequestBody User user) {
        // Validation champs obligatoires
        if (user.getEmail() == null || user.getMotDePasse() == null || user.getRole() == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Champs obligatoires manquants"));
        }

        // Vérification unicité email
        if (userRepo.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.status(409).body(Map.of("message", "Un utilisateur avec cet email existe déjà"));
        }

        User newUser = new User();
        newUser.setNom(user.getNom());
        newUser.setEmail(user.getEmail());
        newUser.setTelephone(user.getTelephone());
        newUser.setMotDePasse(user.getMotDePasse()); // ATTENTION : mot de passe en clair, prévoir encodage
        newUser.setRole(user.getRole());

        userRepo.save(newUser);
        return ResponseEntity.ok(newUser);
    }

    /**
     * Met à jour un utilisateur existant identifié par son ID
     * @param id ID de l’utilisateur à modifier
     * @param user objet utilisateur avec données à mettre à jour
     * @return utilisateur modifié ou message d’erreur
     */
    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User user) {
        Optional<User> optUser = userRepo.findById(id);
        if (optUser.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("message", "Utilisateur introuvable"));
        }

        User existingUser = optUser.get();

        existingUser.setNom(user.getNom());
        existingUser.setEmail(user.getEmail());
        existingUser.setTelephone(user.getTelephone());

        // Mise à jour du mot de passe uniquement si renseigné
        if (user.getMotDePasse() != null && !user.getMotDePasse().isBlank()) {
            existingUser.setMotDePasse(user.getMotDePasse());
        }

        existingUser.setRole(user.getRole());

        userRepo.save(existingUser);
        return ResponseEntity.ok(existingUser);
    }

}
