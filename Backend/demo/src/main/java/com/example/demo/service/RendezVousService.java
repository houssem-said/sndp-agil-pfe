package com.example.demo.service;

import com.example.demo.entity.RendezVous;
import com.example.demo.repository.RendezVousRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Service de gestion des rendez-vous.
 *
 * Cette classe fait le lien entre le contrôleur et le repository,
 * elle encapsule la logique métier liée aux rendez-vous,
 * notamment la création, récupération, mise à jour et suppression.
 */
@Service
public class RendezVousService {

    @Autowired
    private RendezVousRepository rendezVousRepository;

    /**
     * Sauvegarde un rendez-vous (création ou mise à jour).
     * @param rdv Le rendez-vous à sauvegarder.
     * @return Le rendez-vous sauvegardé.
     */
    public RendezVous save(RendezVous rdv) {
        return rendezVousRepository.save(rdv);
    }

    /**
     * Récupère tous les rendez-vous d'un utilisateur donné avec les détails (user et agence).
     * @param userId Identifiant de l'utilisateur.
     * @return Liste des rendez-vous de l'utilisateur.
     */
    public List<RendezVous> findByUserId(Long userId) {
        return rendezVousRepository.findByUserIdWithDetails(userId);
    }

    /**
     * Récupère tous les rendez-vous.
     * @return Liste de tous les rendez-vous.
     */
    public List<RendezVous> findAll() {
        return rendezVousRepository.findAll();
    }

    /**
     * Récupère un rendez-vous par son identifiant.
     * @param id Identifiant du rendez-vous.
     * @return Le rendez-vous ou null s'il n'existe pas.
     */
    public RendezVous getById(Long id) {
        return rendezVousRepository.findById(id).orElse(null);
    }

    /**
     * Supprime un rendez-vous par son identifiant.
     * @param id Identifiant du rendez-vous à supprimer.
     */
    public void delete(Long id) {
        rendezVousRepository.deleteById(id);
    }

    public List<String> getCreneauxPris(Long agenceId, Long serviceId, String date) {
        LocalDate parsedDate = LocalDate.parse(date);
        List<RendezVous> rdvs = rendezVousRepository.findByAgenceIdAndDateAndServiceId(agenceId, parsedDate, serviceId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return rdvs.stream()
                .map(rdv -> rdv.getHeure().format(formatter))  // Conversion LocalTime -> String
                .toList();
    }

    public List<RendezVous> findFutureByUserId(Long userId) {
        return rendezVousRepository.findByUserIdAndDateGreaterThanEqual(userId, LocalDate.now());
    }

}
