package com.sndp.agil.backend.repository;

import com.sndp.agil.backend.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    boolean existsByEmail(String email);  // Vérifie si l'email existe déjà
    Optional<Utilisateur> findByEmail(String email);  // Trouve un utilisateur par email

}
