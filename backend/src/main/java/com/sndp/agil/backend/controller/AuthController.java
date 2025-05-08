package com.sndp.agil.backend.controller;

import com.sndp.agil.backend.dto.AuthResponse;
import com.sndp.agil.backend.dto.LoginRequest;
import com.sndp.agil.backend.model.Utilisateur;
import com.sndp.agil.backend.security.JwtUtils;
import com.sndp.agil.backend.security.UserDetailsImpl;
import com.sndp.agil.backend.service.AuthService;
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

    public AuthController(AuthService authService, JwtUtils jwtUtils, AuthenticationManager authenticationManager) {
        this.authService = authService;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
    }

    // ✅ Inscription d’un utilisateur
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestParam String username,
                                                 @RequestParam String password,
                                                 @RequestParam String email,
                                                 @RequestParam(defaultValue = "CLIENT") String role) {
        Utilisateur newUser = authService.registerUser(username, password, email, role);
        String token = jwtUtils.generateToken(new UserDetailsImpl(newUser));
        return ResponseEntity.ok(new AuthResponse(token, "Bearer", "ROLE_" + newUser.getRole().name()));
    }

    // ✅ Connexion d’un utilisateur
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        // Utiliser les méthodes du record (nommé, pas getX)
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String token = jwtUtils.generateToken(userDetails);

        return ResponseEntity.ok(new AuthResponse(token, "Bearer", userDetails.getAuthorities().toString()));
    }
}
