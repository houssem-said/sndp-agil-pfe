package com.sndp.agil.backend.repository;

import com.sndp.agil.backend.model.ServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ServiceEntityRepository extends JpaRepository<ServiceEntity, Long> {
    List<ServiceEntity> findByNomContainingIgnoreCase(String nom);
}