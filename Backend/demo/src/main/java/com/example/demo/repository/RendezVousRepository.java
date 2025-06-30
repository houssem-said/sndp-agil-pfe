package com.example.demo.repository;

import com.example.demo.entity.RendezVous;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository JPA pour l'entité RendezVous.
 *
 * Étend JpaRepository pour fournir les opérations CRUD standard.
 */
public interface RendezVousRepository extends JpaRepository<RendezVous, Long> {

    /**
     * Recherche les rendez-vous d'un utilisateur donné, en récupérant
     * également les informations détaillées liées à l'utilisateur, l'agence,
     * le service et le guichet via des jointures fetch pour éviter le lazy loading.
     *
     * @param userId L'identifiant de l'utilisateur
     * @return Liste des rendez-vous associés à cet utilisateur, avec détails utilisateur, agence, service et guichet
     */
    @Query("SELECT r FROM RendezVous r " +
            "JOIN FETCH r.user " +
            "JOIN FETCH r.agence " +
            "LEFT JOIN FETCH r.service " +
            "LEFT JOIN FETCH r.guichet " +
            "WHERE r.user.id = :userId AND r.date >= CURRENT_DATE")
    List<RendezVous> findByUserIdWithDetails(@Param("userId") Long userId);

    List<RendezVous> findByAgenceIdAndDateAndServiceId(Long agenceId, LocalDate date, Long serviceId);

    List<RendezVous> findByUserIdAndDateGreaterThanEqual(Long userId, LocalDate date);

    List<RendezVous> findByAgenceIdAndDateAndService_Id(Long agenceId, LocalDate date, Long serviceId);

}
