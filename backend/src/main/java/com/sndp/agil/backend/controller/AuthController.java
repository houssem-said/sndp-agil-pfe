package com.sndp.agil.backend.controller;

import com.sndp.agil.backend.dto.AuthResponse;
import com.sndp.agil.backend.dto.LoginRequest;
import com.sndp.agil.backend.model.Utilisateur;
import com.sndp.agil.backend.security.JwtUtils;
import com.sndp.agil.backend.security.UserDetailsImpl;
import com.sndp.agil.backend.service.AuthService;
import com.sndp.agil.backend.service.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    public AuthController(AuthService authService, JwtUtils jwtUtils, AuthenticationManager authenticationManager, EmailService emailService) {
        this.authService = authService;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestParam String username,
                                           @RequestParam String password,
                                           @RequestParam String email,
                                           @RequestParam(defaultValue = "CLIENT") String role) {
        Utilisateur newUser = authService.registerUser(username, password, email, role);

        // Générer un token de confirmation
        String token = jwtUtils.generateToken(new UserDetailsImpl(newUser));

        // Envoyer un e-mail de confirmation à l'utilisateur
        emailService.sendConfirmationEmail(email, token);

        return ResponseEntity.ok("Utilisateur enregistré. Veuillez vérifier vos e-mails pour confirmer votre compte.");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String token = jwtUtils.generateToken(userDetails);

        // Pour récupérer le rôle au format simple (par exemple ROLE_CLIENT)
        String role = userDetails.getAuthorities().stream()
                .findFirst()
                .map(a -> a.getAuthority())
                .orElse("ROLE_CLIENT");

        return ResponseEntity.ok(new AuthResponse(token, "Bearer", role));
    }


    @PostMapping("/confirm")
    public ResponseEntity<String> confirmAccount(@RequestParam String token) {
        boolean isValid = authService.validateConfirmationToken(token);
        if (isValid) {
            authService.activateUser(token);
            return ResponseEntity.ok("Compte activé !");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token invalide !");
    }

    @PostMapping("/admin/login")
    public ResponseEntity<AuthResponse> adminLogin(@RequestBody LoginRequest request) {
        Utilisateur adminUser = authService.authenticateAdmin(request); // Authentifier comme admin
        String token = jwtUtils.generateToken(new UserDetailsImpl(adminUser));
        return ResponseEntity.ok(new AuthResponse(token, "Bearer", "ROLE_ADMIN"));
    }

    @PostMapping("/agent/login")
    public ResponseEntity<AuthResponse> agentLogin(@RequestBody LoginRequest request) {
        Utilisateur agentUser = authService.authenticateAgent(request); // Authentifier comme agent
        String token = jwtUtils.generateToken(new UserDetailsImpl(agentUser));
        return ResponseEntity.ok(new AuthResponse(token, "Bearer", "ROLE_AGENT"));
    }

    @GetMapping("/me")
    public ResponseEntity<Utilisateur> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = authentication.getName();
        Utilisateur user = authService.findByEmail(email);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        user.setMotDePasse(null);
        return ResponseEntity.ok(user);
    }


}
