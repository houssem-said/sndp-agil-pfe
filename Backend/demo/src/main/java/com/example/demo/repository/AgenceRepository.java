package com.example.demo.repository;

import com.example.demo.entity.Agence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Repository JPA pour l'entité Agence.
 *
 * Étend JpaRepository pour bénéficier des méthodes CRUD standard.
 */
public interface AgenceRepository extends JpaRepository<Agence, Long> {

    /**
     * Recherche une agence par son identifiant en récupérant aussi
     * ses guichets ainsi que les opérateurs associés à ces guichets.
     *
     * Cette requête utilise des fetch joins pour éviter le problème de N+1
     * et récupérer en une seule requête les données liées.
     *
     * @param id Identifiant de l'agence recherchée
     * @return Optional contenant l'agence avec ses guichets et opérateurs si trouvée
     */
    @Query("SELECT a FROM Agence a LEFT JOIN FETCH a.guichets g LEFT JOIN FETCH g.operateur WHERE a.id = :id")
    Optional<Agence> findByIdWithGuichetsAndOperateurs(@Param("id") Long id);
}
