package com.sndp.agil.backend.service;

import com.sndp.agil.backend.exception.ConflitRendezVousException;
import com.sndp.agil.backend.model.RendezVous;
import com.sndp.agil.backend.model.StatutRendezVous;
import com.sndp.agil.backend.repository.RendezVousRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RendezVousService {
    private final RendezVousRepository rendezVousRepository;

    public RendezVousService(RendezVousRepository rendezVousRepository) {
        this.rendezVousRepository = rendezVousRepository;
    }

    @Transactional
    public RendezVous creerRDV(RendezVous rdv) {
        if (rendezVousRepository.existsByDateHeureAndServiceId(
                rdv.getDateHeure(),
                rdv.getService().getId()
        )) {
            throw new ConflitRendezVousException(
                    rdv.getDateHeure(),
                    rdv.getService().getId()
            );
        }
        rdv.setStatut(StatutRendezVous.CONFIRME);
        return rendezVousRepository.save(rdv);
    }

    @Transactional(readOnly = true)
    public List<RendezVous> getRDVParJour(LocalDateTime date) {
        return rendezVousRepository.findByDateHeureBetween(
                date.withHour(0).withMinute(0),
                date.withHour(23).withMinute(59)
        );
    }
}