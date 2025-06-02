package com.sndp.agil.backend.controller;

import com.sndp.agil.backend.dto.UserCreateRequest;
import com.sndp.agil.backend.dto.UserDTO;
import com.sndp.agil.backend.model.User;
import com.sndp.agil.backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

/**
 * Contrôleur pour gérer les opérations liées aux utilisateurs (création et affichage du profil).
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Création d’un nouvel utilisateur (CLIENT, AGENT ou ADMIN).
     * POST /api/users
     * Body JSON conforme à UserCreateRequest { username, email, password, role }
     */
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserCreateRequest request) {
        User savedUser = userService.createUser(request);
        // Renvoie 201 Created avec l’URI du nouvel utilisateur
        return ResponseEntity
                .created(URI.create("/api/users/" + savedUser.getId()))
                .body(savedUser);
    }

    /**
     * Récupère un utilisateur par son ID.
     * GET /api/users/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        // On masque le mot de passe avant de renvoyer
        user.setMotDePasse(null);
        return ResponseEntity.ok(user);
    }

    /**
     * Récupère le profil de l’utilisateur connecté.
     * GET /api/users/me
     */
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String email = authentication.getName();
        UserDTO userDTO = userService.getUserDTOByEmail(email);
        return ResponseEntity.ok(userDTO);
    }
}
