package com.example.demo.repository;

import com.example.demo.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository JPA pour l'entité Ticket.
 *
 * Fournit les méthodes d'accès aux tickets en base de données.
 */
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    /**
     * Récupère tous les tickets associés à un utilisateur donné.
     *
     * @param userId L'identifiant de l'utilisateur
     * @return Liste des tickets liés à cet utilisateur
     */
    List<Ticket> findByUserId(Long userId);

    /**
     * Compte le nombre de tickets non encore appelés pour un guichet spécifique.
     *
     * @param guichetId L'identifiant du guichet
     * @return Le nombre de tickets non appelés pour ce guichet
     */
    long countByGuichetIdAndAppeleFalse(Long guichetId);

    List<Ticket> findByAgenceIdAndDateTicketBetween(Long agenceId, LocalDateTime start, LocalDateTime end);

}
