package com.example.demo.repository;

import com.example.demo.entity.Guichet;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repository JPA pour l'entité Guichet.
 *
 * Étend JpaRepository pour fournir les opérations CRUD standard.
 */
public interface GuichetRepository extends JpaRepository<Guichet, Long> {

    List<Guichet> findByAgenceId(Long agenceId);

    Optional<Guichet> findByOperateur(User operateur);

    /**
     * Trouve le guichet ouvert avec la file d'attente la plus courte pour une agence donnée.
     * Ici on compte le nombre de tickets en attente (exemple avec alias t).
     */
    @Query("SELECT g FROM Guichet g LEFT JOIN g.tickets t " +
            "WHERE g.agence.id = :agenceId AND g.ouvert = true " +
            "GROUP BY g.id ORDER BY COUNT(t) ASC")
    List<Guichet> findOpenGuichetsOrderByTicketsCountAsc(@Param("agenceId") Long agenceId);
}
