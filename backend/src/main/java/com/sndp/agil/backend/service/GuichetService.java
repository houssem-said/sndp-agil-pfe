package com.sndp.agil.backend.service;

import com.sndp.agil.backend.model.Guichet;
import com.sndp.agil.backend.repository.GuichetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GuichetService {
    private final GuichetRepository guichetRepository;

    public Guichet getGuichetById(Long id) {
        return guichetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Guichet non trouvé"));
    }
}