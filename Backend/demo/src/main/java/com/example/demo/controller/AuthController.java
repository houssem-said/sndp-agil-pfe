package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth") // Toutes les routes de ce contrôleur commencent par /api/auth
@CrossOrigin(origins = "*") // Autorise les requêtes cross-origin depuis toutes origines (attention en prod)
public class AuthController {

    @Autowired
    private UserRepository userRepository; // Injection du repository utilisateur pour accès DB

    @Autowired
    private UserService userService; // Service métier utilisateur (logique métier)

    /**
     * Endpoint de connexion (login) utilisateur
     * @param credentials Map JSON contenant "email" et "motDePasse"
     * @return 200 avec infos utilisateur si OK, 401 sinon
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String motDePasse = credentials.get("motDePasse");

        // Recherche utilisateur par email
        User user = userService.findByEmail(email);

        // Vérification mot de passe en clair (attention : sécurité faible)
        if (user != null && user.getMotDePasse().equals(motDePasse)) {
            // Préparation de la réponse avec données utiles du user
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Connexion réussie");
            response.put("id", user.getId());
            response.put("nom", user.getNom() != null ? user.getNom() : "");
            response.put("email", user.getEmail() != null ? user.getEmail() : "");
            response.put("role", user.getRole() != null ? user.getRole() : "");

            return ResponseEntity.ok(response); // 200 OK
        }

        // En cas d'échec, on renvoie un 401 Unauthorized avec un message d'erreur
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Email ou mot de passe invalide"));
    }

    /**
     * Endpoint d'inscription (registration) d'un nouvel utilisateur
     * @param user objet User reçu en JSON (nom, email, motDePasse, etc.)
     * @return 200 avec confirmation ou 409 si email déjà utilisé
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        // Vérifie que l’email n’existe pas déjà en base pour éviter doublon
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "Email déjà utilisé")); // 409 Conflict
        }

        user.setRole("USER");  // Attribution du rôle "USER" par défaut à l'inscription

        User savedUser = userRepository.save(user); // Sauvegarde en base

        // Préparation de la réponse de succès
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Utilisateur créé avec succès");
        response.put("userId", savedUser.getId());

        return ResponseEntity.ok(response); // 200 OK
    }
}
