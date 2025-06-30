package com.example.demo.repository;

import com.example.demo.entity.ServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository JPA pour l'entité ServiceEntity.
 *
 * Étend JpaRepository pour fournir les opérations CRUD standard
 * sur les services (ex: créer, lire, mettre à jour, supprimer).
 */
public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {
    // Pas de méthodes personnalisées nécessaire,
    // JpaRepository fournit déjà les opérations de base.
}
