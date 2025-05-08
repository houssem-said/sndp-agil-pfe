package com.sndp.agil.backend.service;

import com.sndp.agil.backend.dto.AuthResponse;
import com.sndp.agil.backend.dto.LoginRequest;
import com.sndp.agil.backend.model.RoleUtilisateur;
import com.sndp.agil.backend.model.Utilisateur;
import com.sndp.agil.backend.repository.UtilisateurRepository;
import com.sndp.agil.backend.security.JwtUtils;
import com.sndp.agil.backend.security.UserDetailsImpl;
import com.sndp.agil.backend.service.EmailService;  // Importer le service d'email
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final EmailService emailService;  // Injection du service d'email

    public AuthService(UtilisateurRepository utilisateurRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtils jwtUtils,
                       EmailService emailService) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.emailService = emailService;  // Initialisation du service d'email
    }

    // Inscription d'un utilisateur
    public Utilisateur registerUser(String username, String password, String email, String role) {
        // Validation de l'email
        if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new IllegalArgumentException("Email invalide");
        }

        // Validation de la longueur du mot de passe
        if (password.length() < 8) {
            throw new IllegalArgumentException("Le mot de passe doit contenir au moins 8 caractères");
        }

        // Création de l'utilisateur
        RoleUtilisateur userRole = RoleUtilisateur.valueOf(role.toUpperCase());
        Utilisateur utilisateur = new Utilisateur(username, passwordEncoder.encode(password), email, userRole);
        utilisateur = utilisateurRepository.save(utilisateur);

        // Générer un token de confirmation (par exemple JWT)
        String token = jwtUtils.generateToken(new UserDetailsImpl(utilisateur));

        // Envoyer un e-mail de confirmation à l'utilisateur
        emailService.sendConfirmationEmail(email, token);

        return utilisateur;
    }

    // Authentification d'un utilisateur
    public AuthResponse authenticate(LoginRequest request) {
        Utilisateur user = utilisateurRepository.findByEmail(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (!passwordEncoder.matches(request.getPassword(), user.getMotDePasse())) {
            throw new RuntimeException("Mot de passe incorrect");
        }

        String token = jwtUtils.generateToken(new UserDetailsImpl(user));
        return new AuthResponse(token, "Bearer", user.getRole().name());
    }

    // Authentification d'un administrateur
    public Utilisateur authenticateAdmin(LoginRequest request) {
        Utilisateur user = utilisateurRepository.findByEmail(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Admin non trouvé"));
        if (!user.getRole().equals(RoleUtilisateur.ADMIN)) {
            throw new RuntimeException("Accès réservé aux administrateurs !");
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getMotDePasse())) {
            throw new RuntimeException("Mot de passe incorrect");
        }
        return user;
    }

    // Authentification d'un agent
    public Utilisateur authenticateAgent(LoginRequest request) {
        Utilisateur user = utilisateurRepository.findByEmail(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Agent non trouvé"));
        if (!user.getRole().equals(RoleUtilisateur.AGENT)) {
            throw new RuntimeException("Accès réservé aux agents !");
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getMotDePasse())) {
            throw new RuntimeException("Mot de passe incorrect");
        }
        return user;
    }

    // Validation du token de confirmation
    public boolean validateConfirmationToken(String token) {
        try {
            return jwtUtils.validateToken(token);  // Vérifie la validité du token
        } catch (Exception e) {
            return false;  // Si une exception est lancée, le token est invalide
        }
    }

    // Activation de l'utilisateur
    public void activateUser(String token) {
        String email = jwtUtils.getUsernameFromToken(token);

        // Trouver l'utilisateur dans la base de données
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // Activer l'utilisateur
        utilisateur.setActive(true);
        utilisateurRepository.save(utilisateur);
    }

}
