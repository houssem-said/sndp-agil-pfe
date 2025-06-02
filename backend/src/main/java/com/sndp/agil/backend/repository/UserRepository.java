package com.sndp.agil.backend.repository;

import com.sndp.agil.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);  // Vérifie si l'email existe déjà
    Optional<User> findByEmail(String email);  // Trouve un utilisateur par email

}
