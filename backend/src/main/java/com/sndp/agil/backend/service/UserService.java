package com.sndp.agil.backend.service;

import com.sndp.agil.backend.dto.UserCreateRequest;
import com.sndp.agil.backend.dto.UserDTO;
import com.sndp.agil.backend.exception.UserAlreadyExistsException;
import com.sndp.agil.backend.model.User;
import com.sndp.agil.backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Crée un nouvel utilisateur d'après le DTO UserCreateRequest.
     * Vérifie que l'email n'existe pas déjà, encode le mot de passe
     * et assigne le rôle indiqué. Renvoie l'entité persistée.
     */
    @Transactional
    public User createUser(UserCreateRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new UserAlreadyExistsException("Email déjà utilisé");
        }

        User user = new User();
        user.setNom(request.username()); // Associe le nom complet
        user.setEmail(request.email());
        user.setMotDePasse(passwordEncoder.encode(request.password()));
        user.setRole(request.role());
        user.setActive(false);

        return userRepository.save(user);
    }

    /**
     * Récupère un utilisateur par son ID.
     */
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    /**
     * Récupère un utilisateur par email, jette si introuvable.
     */
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User non trouvé"));
    }

    /**
     * Transforme l'entité User en UserDTO pour l’API (profil).
     */
    public UserDTO getUserDTOByEmail(String email) {
        User user = findByEmail(email);
        return new UserDTO(user.getNom(), user.getEmail(), user.getRole().name());
    }
}
