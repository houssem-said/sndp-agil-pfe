package com.example.demo.service;

import com.example.demo.entity.Agence;
import com.example.demo.repository.AgenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service pour gérer la logique métier liée aux agences.
 *
 * Cette classe sert d'intermédiaire entre le contrôleur et le repository,
 * en encapsulant les opérations CRUD sur les agences.
 */
@Service
public class AgenceService {

    @Autowired
    private AgenceRepository agenceRepository;

    /**
     * Sauvegarde ou met à jour une agence en base de données.
     *
     * @param agence L'agence à sauvegarder
     * @return L'agence sauvegardée avec son ID généré si création
     */
    public Agence save(Agence agence) {
        return agenceRepository.save(agence);
    }

    /**
     * Récupère la liste de toutes les agences.
     *
     * @return Liste de toutes les agences existantes
     */
    public List<Agence> findAll() {
        return agenceRepository.findAll();
    }

    /**
     * Recherche une agence par son identifiant,
     * en récupérant aussi les guichets et opérateurs associés.
     *
     * @param id Identifiant de l'agence recherchée
     * @return Optional contenant l'agence si trouvée, sinon vide
     */
    public Optional<Agence> getById(Long id) {
        return agenceRepository.findByIdWithGuichetsAndOperateurs(id);
    }

    /**
     * Supprime une agence par son identifiant.
     *
     * @param id Identifiant de l'agence à supprimer
     */
    public void delete(Long id) {
        agenceRepository.deleteById(id);
    }
}
