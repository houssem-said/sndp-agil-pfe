package com.sndp.agil.backend.service;

import com.sndp.agil.backend.exception.ConflitRendezVousException;
import com.sndp.agil.backend.model.RendezVous;
import com.sndp.agil.backend.model.ServiceEntity;
import com.sndp.agil.backend.repository.RendezVousRepository;
import com.sndp.agil.backend.repository.ServiceEntityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RendezVousService {

    private final RendezVousRepository rendezVousRepository;
    private final ServiceEntityRepository serviceEntityRepository;

    public RendezVousService(RendezVousRepository rendezVousRepository,
                             ServiceEntityRepository serviceEntityRepository) {
        this.rendezVousRepository = rendezVousRepository;
        this.serviceEntityRepository = serviceEntityRepository;
    }

    /**
     * Crée un rendez-vous après vérification de conflit sur la même date/heure et service.
     */
    @Transactional
    public RendezVous creerRDV(RendezVous rdv) {
        LocalDateTime dateHeure = rdv.getDateHeure();
        Long serviceId = rdv.getService().getId();

        if (rendezVousRepository.existsByDateHeureAndServiceId(dateHeure, serviceId)) {
            throw new ConflitRendezVousException(dateHeure, serviceId);
        }

        // Correction ici : tu accèdes à la valeur par défaut StatutRendezVous.CONFIRME via l'enum directement, pas via une instance
        if (rdv.getStatut() == null) {
            rdv.setStatut(com.sndp.agil.backend.model.StatutRendezVous.CONFIRME);
        }

        return rendezVousRepository.save(rdv);
    }

    /**
     * Récupère tous les rendez-vous d’un utilisateur (par ID utilisateur).
     */
    public List<RendezVous> getByUser(Long utilisateurId) {
        return rendezVousRepository.findByUserId(utilisateurId);
    }

    /**
     * Récupère tous les rendez-vous pour un service donné (ID service).
     */
    public List<RendezVous> getByServiceId(Long serviceId) {
        return rendezVousRepository.findByServiceId(serviceId);
    }

    /**
     * Renvoie un objet ServiceEntity par son ID, ou lève une exception si introuvable.
     */
    public ServiceEntity findServiceById(Long serviceId) {
        return serviceEntityRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("ServiceEntity introuvable"));
    }

    /**
     * Récupère tous les rendez-vous dont la dateHeure est entre date 00:00:00 et 23:59:59.
     */
    public List<RendezVous> getRDVParJour(LocalDateTime date) {
        LocalDateTime debutJour = date.toLocalDate().atStartOfDay();
        LocalDateTime finJour = debutJour.plusDays(1).minusNanos(1);
        return rendezVousRepository.findByDateHeureBetween(debutJour, finJour);
    }
}
