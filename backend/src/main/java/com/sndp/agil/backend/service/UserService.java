package com.sndp.agil.backend.service;

import com.sndp.agil.backend.dto.UserCreateRequest;
import com.sndp.agil.backend.dto.UserDTO;
import com.sndp.agil.backend.exception.UserAlreadyExistsException;
import com.sndp.agil.backend.model.Utilisateur;
import com.sndp.agil.backend.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Utilisateur createUser(UserCreateRequest request) {
        if (utilisateurRepository.existsByEmail(request.email())) {
            throw new UserAlreadyExistsException("Email déjà utilisé");
        }

        Utilisateur user = new Utilisateur();
        user.setEmail(request.email());
        user.setMotDePasse(passwordEncoder.encode(request.password()));
        user.setRole(request.role());

        return utilisateurRepository.save(user);
    }

    public Utilisateur getUserById(Long id) {
        return utilisateurRepository.findById(id).orElse(null);
    }

    public UserDTO findByEmail(String email) {
        Utilisateur user = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        return new UserDTO(user.getEmail(), user.getEmail(), user.getRole().name());
    }
}
