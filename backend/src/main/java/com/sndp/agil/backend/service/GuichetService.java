package com.sndp.agil.backend.service;

import com.sndp.agil.backend.model.Guichet;
import com.sndp.agil.backend.repository.GuichetRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GuichetService {

    private final GuichetRepository guichetRepository;

    public GuichetService(GuichetRepository guichetRepository) {
        this.guichetRepository = guichetRepository;
    }

    /**
     * Récupère un guichet par son ID, ou lève une exception si introuvable.
     */
    public Guichet getGuichetById(Long id) {
        return guichetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Guichet non trouvé : " + id));
    }

    /**
     * Récupère le premier guichet actif (ex : pour assigner automatiquement un ticket au client).
     * Lève une exception si aucun guichet actif n’est disponible.
     */
    public Guichet getFirstActiveGuichet() {
        List<Guichet> actifs = guichetRepository.findByEstActifTrue();
        return actifs.stream().findFirst()
                .orElseThrow(() -> new RuntimeException("Aucun guichet actif disponible"));
    }

    /**
     * Récupère tous les guichets d’un service donné.
     */
    public List<Guichet> getGuichetsByServiceId(Long serviceId) {
        return guichetRepository.findByServiceId(serviceId);
    }
}
