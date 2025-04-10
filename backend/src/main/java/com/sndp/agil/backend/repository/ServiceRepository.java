package com.sndp.agil.backend.repository;

import com.sndp.agil.backend.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ServiceRepository extends JpaRepository<Service, Long> {
    List<Service> findByNomContainingIgnoreCase(String nom);
}