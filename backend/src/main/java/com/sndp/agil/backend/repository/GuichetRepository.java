package com.sndp.agil.backend.repository;

import com.sndp.agil.backend.model.Guichet;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GuichetRepository extends JpaRepository<Guichet, Long> {
    List<Guichet> findByServiceId(Long serviceId);
    List<Guichet> findByEstActifTrue();
}