package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repository JPA pour l'entité User.
 *
 * Fournit des méthodes personnalisées pour la recherche d'utilisateurs en base de données.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Recherche un utilisateur par son adresse email.
     *
     * @param email L'adresse email à rechercher
     * @return Un Optional contenant l'utilisateur si trouvé, sinon vide
     */
    Optional<User> findByEmail(String email);

    /**
     * Compte le nombre d'utilisateurs possédant un rôle donné.
     *
     * @param role Le rôle à rechercher (ex: "ADMIN", "OPERATEUR", "USER")
     * @return Le nombre d'utilisateurs avec ce rôle
     */
    long countByRole(String role);

    /**
     * Récupère la liste des utilisateurs ayant un rôle donné.
     *
     * @param role Le rôle à rechercher
     * @return Liste des utilisateurs correspondant au rôle
     */
    List<User> findByRole(String role);

    /**
     * Recherche un utilisateur par son ID en récupérant également
     * les informations associées au guichet et aux tickets liés,
     * afin d'éviter le problème de lazy loading.
     *
     * @param id L'identifiant de l'utilisateur
     * @return Un Optional contenant l'utilisateur avec guichet et tickets chargés
     */
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.guichet g LEFT JOIN FETCH g.tickets WHERE u.id = :id")
    Optional<User> findByIdWithGuichetAndTickets(@Param("id") Long id);

}
