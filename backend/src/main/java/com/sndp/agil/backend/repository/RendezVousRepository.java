package com.sndp.agil.backend.repository;

import com.sndp.agil.backend.model.RendezVous;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface RendezVousRepository extends JpaRepository<RendezVous, Long> {
    List<RendezVous> findByDateHeureBetween(LocalDateTime start, LocalDateTime end);
    List<RendezVous> findByUserId(Long userId);
    boolean existsByDateHeureAndServiceId(LocalDateTime dateHeure, Long serviceId);
    List<RendezVous> findByServiceId(Long serviceId);
}