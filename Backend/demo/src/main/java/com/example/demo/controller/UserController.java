package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour la gestion des utilisateurs.
 * Gère les opérations CRUD de base : création, lecture, mise à jour et suppression,
 * ainsi que la récupération filtrée par rôle (ex : opérateurs).
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Récupère la liste de tous les utilisateurs.
     *
     * @return liste d'utilisateurs
     */
    @GetMapping
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    /**
     * Récupère un utilisateur par son identifiant.
     *
     * @param id identifiant de l'utilisateur
     * @return utilisateur trouvé ou 404 si introuvable
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    /**
     * Crée un nouvel utilisateur.
     *
     * @param user objet utilisateur à créer (reçu en JSON)
     * @return utilisateur sauvegardé avec code 201
     */
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User savedUser = userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    /**
     * Met à jour un utilisateur existant à partir de son identifiant.
     *
     * @param id identifiant de l'utilisateur à modifier
     * @param updatedUser objet contenant les nouvelles données
     * @return utilisateur mis à jour ou 404 si non trouvé
     */
    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        User user = userService.getById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        user.setNom(updatedUser.getNom());
        user.setEmail(updatedUser.getEmail());
        user.setTelephone(updatedUser.getTelephone());

        if (updatedUser.getMotDePasse() != null && !updatedUser.getMotDePasse().isEmpty()) {
            user.setMotDePasse(updatedUser.getMotDePasse());
        }

        if (updatedUser.getGuichet() != null) {
            user.setGuichet(updatedUser.getGuichet());
        }

        User savedUser = userService.save(user);
        return ResponseEntity.ok(savedUser);
    }

    /**
     * Récupère uniquement les utilisateurs ayant le rôle "OPERATEUR".
     *
     * @return liste des opérateurs
     */
    @GetMapping("/operateurs")
    public List<User> getOperateurs() {
        return userService.findByRole("OPERATEUR");
    }

    /**
     * Supprime un utilisateur par son identifiant.
     *
     * @param id identifiant de l'utilisateur à supprimer
     * @return 204 No Content si supprimé, 404 si non trouvé
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        boolean deleted = userService.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
