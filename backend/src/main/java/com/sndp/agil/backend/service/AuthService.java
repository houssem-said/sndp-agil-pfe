package com.sndp.agil.backend.service;

import com.sndp.agil.backend.dto.LoginRequest;
import com.sndp.agil.backend.model.RoleUtilisateur;
import com.sndp.agil.backend.model.User;
import com.sndp.agil.backend.repository.UserRepository;
import com.sndp.agil.backend.security.JwtUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final EmailService emailService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtils jwtUtils,
                       EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.emailService = emailService;
    }

    /**
     * Inscription d’un nouvel utilisateur.
     * Vérifie les contraintes (email, mot de passe, rôle), sauvegarde l’utilisateur
     * puis envoie un e-mail de confirmation contenant le token JWT.
     */
    public User registerUser(String username, String password, String email, String role) {
        // Validation de l’email via regex simple
        if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new IllegalArgumentException("Email invalide");
        }

        // Vérification de la longueur minimale du mot de passe
        if (password.length() < 8) {
            throw new IllegalArgumentException("Le mot de passe doit contenir au moins 8 caractères");
        }

        // Vérifier si l’email existe déjà
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email déjà utilisé");
        }

        // Converter role du frontend (STRING) en RoleUtilisateur
        RoleUtilisateur userRole;
        try {
            userRole = RoleUtilisateur.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Rôle user invalide");
        }

        // Création de l’user (non actif par défaut, activation après mail)
        User user = new User(username, passwordEncoder.encode(password), email, userRole);
        user.setActive(false);
        user = userRepository.save(user);

        return user;
    }

    /**
     * Vérification du token de confirmation (JWT).
     */
    public boolean validateConfirmationToken(String token) {
        try {
            return jwtUtils.validateToken(token);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Activation de l’utilisateur après confirmation.
     * On extrait l’email du token, on met à jour le champ 'active' à true.
     */
    public void activateUser(String token) {
        String email = jwtUtils.extractUsername(token);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User non trouvé"));
        user.setActive(true);
        userRepository.save(user);
    }

    /**
     * Envoi d’un e-mail de confirmation contenant le lien d’activation.
     * (La génération du token se fait dans AuthController après save).
     */
    public void sendConfirmationEmail(String email, String token) {
        emailService.sendConfirmationEmail(email, token);
    }

    /**
     * Authentification standard (email + mot de passe).
     * Utilisé par AuthController pour login.
     * Retourne un AuthResponse dans AuthController, donc ici on ne gère que la logique métier.
     */
    public User authenticate(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User non trouvé"));

        if (!passwordEncoder.matches(request.getPassword(), user.getMotDePasse())) {
            throw new RuntimeException("Mot de passe incorrect");
        }
        return user;
    }

    /**
     * Authentification réservée à un administrateur.
     */
    public User authenticateAdmin(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User non trouvé"));
        if (!user.getRole().equals(RoleUtilisateur.ADMIN)) {
            throw new RuntimeException("Accès réservé aux administrateurs !");
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getMotDePasse())) {
            throw new RuntimeException("Mot de passe incorrect");
        }
        return user;
    }

    /**
     * Authentification réservée à un agent.
     */
    public User authenticateAgent(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User non trouvé"));
        if (!user.getRole().equals(RoleUtilisateur.AGENT)) {
            throw new RuntimeException("Accès réservé aux agents !");
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getMotDePasse())) {
            throw new RuntimeException("Mot de passe incorrect");
        }
        return user;
    }
}
