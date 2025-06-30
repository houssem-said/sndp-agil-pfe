package com.example.demo.service;

import com.example.demo.entity.Guichet;
import com.example.demo.entity.User;
import com.example.demo.repository.GuichetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service de gestion des guichets.
 *
 * Cette classe fait le lien entre le contrôleur et le repository,
 * et contient la logique métier liée aux guichets.
 *
 * Elle permet notamment de créer, mettre à jour, supprimer et récupérer les guichets,
 * ainsi que d'assigner un opérateur à un guichet tout en garantissant qu'un opérateur
 * ne soit lié qu'à un seul guichet à la fois.
 */
@Service
public class GuichetService {

    @Autowired
    private GuichetRepository guichetRepository;

    /**
     * Sauvegarde un guichet en base (création ou mise à jour).
     * @param guichet Le guichet à enregistrer.
     * @return Le guichet sauvegardé.
     */
    public Guichet save(Guichet guichet) {
        return guichetRepository.save(guichet);
    }

    /**
     * Récupère la liste de tous les guichets.
     * @return Liste des guichets.
     */
    public List<Guichet> findAll() {
        return guichetRepository.findAll();
    }

    /**
     * Récupère un guichet par son identifiant.
     * @param id Identifiant du guichet.
     * @return Le guichet ou null s'il n'existe pas.
     */
    public Guichet getById(Long id) {
        return guichetRepository.findById(id).orElse(null);
    }

    /**
     * Supprime un guichet par son identifiant.
     * @param id Identifiant du guichet à supprimer.
     */
    @Transactional
    public void delete(Long id) {
        Guichet guichet = guichetRepository.findById(id).orElse(null);
        if (guichet == null) return;

        // Désassocier opérateur
        if (guichet.getOperateur() != null) {
            guichet.setOperateur(null);
        }

        // Si le guichet a des tickets associés → suppression ou nettoyage
        if (guichet.getTickets() != null) {
            guichet.getTickets().clear();
        }

        // Supprimer le guichet après nettoyage
        guichetRepository.delete(guichet);
    }


    /**
     * Récupère tous les guichets associés à une agence donnée.
     * @param agenceId Identifiant de l'agence.
     * @return Liste des guichets de l'agence.
     */
    public List<Guichet> findByAgenceId(Long agenceId) {
        return guichetRepository.findByAgenceId(agenceId);
    }

    /**
     * Assigne un opérateur à un guichet spécifique.
     *
     * Si l'opérateur est déjà assigné à un autre guichet, le dissocie d'abord de ce guichet.
     * Cette méthode garantit qu'un opérateur n'est lié qu'à un seul guichet à la fois.
     *
     * @param guichetId Identifiant du guichet cible.
     * @param operateur L'opérateur à assigner.
     * @return Le guichet mis à jour avec l'opérateur assigné.
     * @throws RuntimeException si le guichet cible n'existe pas.
     */
    @Transactional
    public Guichet assignOperateurToGuichet(Long guichetId, User operateur) {
        Optional<Guichet> ancienGuichetOpt = guichetRepository.findByOperateur(operateur);
        if (ancienGuichetOpt.isPresent()) {
            Guichet ancienGuichet = ancienGuichetOpt.get();
            if (!ancienGuichet.getId().equals(guichetId)) {
                // Dissocier l'opérateur de son ancien guichet avant de le réassigner
                ancienGuichet.setOperateur(null);
                guichetRepository.save(ancienGuichet);
            }
        }

        Guichet guichet = guichetRepository.findById(guichetId)
                .orElseThrow(() -> new RuntimeException("Guichet non trouvé avec id " + guichetId));

        guichet.setOperateur(operateur);
        return guichetRepository.save(guichet);
    }

    /**
     * Crée ou met à jour un guichet, et assigne un opérateur.
     *
     * Si un guichet existe déjà avec l'opérateur donné, il est dissocié.
     * Si guichetId est null, un nouveau guichet est créé.
     * Sinon, le guichet existant est mis à jour avec les nouvelles données et l'opérateur.
     *
     * @param guichetId Identifiant du guichet à modifier, ou null pour créer.
     * @param operateur Opérateur à assigner au guichet.
     * @param guichetData Données du guichet à créer ou mettre à jour.
     * @return Le guichet créé ou mis à jour.
     */
    @Transactional
    public Guichet assignOperateurToGuichet(Long guichetId, User operateur, Guichet guichetData) {
        // Dissocier l'opérateur de tout guichet existant
        Optional<Guichet> ancien = guichetRepository.findByOperateur(operateur);
        ancien.ifPresent(old -> {
            old.setOperateur(null);
            guichetRepository.save(old);
        });

        Guichet guichet;

        if (guichetId != null) {
            // Mise à jour d'un guichet existant
            guichet = guichetRepository.findById(guichetId)
                    .orElseThrow(() -> new RuntimeException("Guichet non trouvé avec id " + guichetId));
            guichet.setNom(guichetData.getNom());
            guichet.setOuvert(guichetData.isOuvert());
            guichet.setAgence(guichetData.getAgence());
        } else {
            // Création d'un nouveau guichet
            guichet = new Guichet();
            guichet.setNom(guichetData.getNom());
            guichet.setOuvert(guichetData.isOuvert());
            guichet.setAgence(guichetData.getAgence());
        }

        guichet.setOperateur(operateur);
        return guichetRepository.save(guichet);
    }
}
