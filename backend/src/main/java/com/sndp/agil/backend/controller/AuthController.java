package com.sndp.agil.backend.controller;

import com.sndp.agil.backend.dto.AuthResponse;
import com.sndp.agil.backend.dto.LoginRequest;
import com.sndp.agil.backend.model.User;
import com.sndp.agil.backend.security.JwtUtils;
import com.sndp.agil.backend.security.UserDetailsImpl;
import com.sndp.agil.backend.service.AuthService;
import com.sndp.agil.backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur pour gérer l’authentification, l’inscription et la confirmation par email.
 * Les URLs exposées sont sous /api/auth/... (gestionnée par spring.mvc.servlet.path=/api).
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;  // Utilisé pour getCurrentUser

    public AuthController(AuthService authService,
                          JwtUtils jwtUtils,
                          AuthenticationManager authenticationManager,
                          UserService userService) {
        this.authService = authService;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    /**
     * Inscription d’un nouvel utilisateur (CLIENT par défaut ou rôle fourni).
     * POST /api/auth/register
     * Body form-url-encoded : username, password, email, role (optionnel)
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestParam String username,
                                           @RequestParam String password,
                                           @RequestParam String email,
                                           @RequestParam(defaultValue = "CLIENT") String role) {
        User newUser = authService.registerUser(username, password, email, role);
        // Génération d’un token de confirmation
        String token = jwtUtils.generateToken(new UserDetailsImpl(newUser));
        // Envoi d’un email de confirmation
        authService.sendConfirmationEmail(email, token);
        return ResponseEntity.ok("User enregistré. Veuillez vérifier vos e-mails pour confirmer votre compte.");
    }

    /**
     * Connexion d’un utilisateur/agent/admin.
     * POST /api/auth/login
     * Body JSON : { "email": "...", "password": "..." }
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String token = jwtUtils.generateToken(userDetails);

        // Extraction du rôle (ex. "ROLE_CLIENT" → "CLIENT")
        String role = userDetails.getAuthorities().stream()
                .findFirst()
                .map(auth -> auth.getAuthority().replaceFirst("^ROLE_", ""))
                .orElse("CLIENT");

        return ResponseEntity.ok(new AuthResponse(token, "Bearer", role));
    }

    /**
     * Confirmation du compte après clic sur le lien email.
     * POST /api/auth/confirm?token=...
     */
    @PostMapping("/confirm")
    public ResponseEntity<String> confirmAccount(@RequestParam String token) {
        boolean isValid = authService.validateConfirmationToken(token);
        if (isValid) {
            authService.activateUser(token);
            return ResponseEntity.ok("Compte activé !");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token invalide !");
    }

    /**
     * Récupérer les informations de l’utilisateur connecté.
     * GET /api/auth/me
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Non authentifié");
        }

        String email = authentication.getName();
        User user = userService.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User non trouvé");
        }

        // On masque le mot de passe avant de renvoyer l’objet
        user.setMotDePasse(null);
        return ResponseEntity.ok(user);
    }
}
