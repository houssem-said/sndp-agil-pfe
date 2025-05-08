package com.sndp.agil.backend.service;

import com.sndp.agil.backend.dto.AuthResponse;
import com.sndp.agil.backend.dto.LoginRequest;
import com.sndp.agil.backend.model.RoleUtilisateur;
import com.sndp.agil.backend.model.Utilisateur;
import com.sndp.agil.backend.repository.UtilisateurRepository;
import com.sndp.agil.backend.security.JwtUtils;
import com.sndp.agil.backend.security.UserDetailsImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils; // ✅ Ajout de JwtUtils

    // ✅ Constructeur avec JwtUtils
    public AuthService(UtilisateurRepository utilisateurRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtils jwtUtils) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    // ✅ Enregistrement d'un nouvel utilisateur
    public Utilisateur registerUser(String username, String password, String email, String role) {
        RoleUtilisateur userRole = RoleUtilisateur.valueOf(role.toUpperCase());
        Utilisateur utilisateur = new Utilisateur(username, passwordEncoder.encode(password), email, userRole);
        return utilisateurRepository.save(utilisateur);
    }

    // ✅ Authentification de l'utilisateur
    public AuthResponse authenticate(LoginRequest request) {
        Utilisateur user = utilisateurRepository.findByEmail(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (!passwordEncoder.matches(request.getPassword(), user.getMotDePasse())) {
            throw new RuntimeException("Mot de passe incorrect");
        }

        String token = jwtUtils.generateToken(new UserDetailsImpl(user));
        return new AuthResponse(token, "Bearer", user.getRole().name());
    }
}
