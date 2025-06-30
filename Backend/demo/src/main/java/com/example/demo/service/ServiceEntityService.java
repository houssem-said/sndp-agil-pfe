package com.example.demo.service;

import com.example.demo.entity.ServiceEntity;
import com.example.demo.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service dédié à la gestion des entités ServiceEntity.
 *
 * Cette classe sert d'intermédiaire entre les contrôleurs et le repository,
 * encapsulant la logique métier associée aux opérations CRUD sur les services.
 */
@Service
public class ServiceEntityService {

    @Autowired
    private ServiceRepository serviceRepository;

    /**
     * Récupère la liste complète des services.
     * @return Liste de tous les services.
     */
    public List<ServiceEntity> findAll() {
        return serviceRepository.findAll();
    }

    /**
     * Sauvegarde un service en base de données.
     * Utilisé aussi bien pour la création que pour la mise à jour.
     * @param service L'entité ServiceEntity à sauvegarder.
     * @return Le service sauvegardé.
     */
    public ServiceEntity save(ServiceEntity service) {
        return serviceRepository.save(service);
    }

    /**
     * Récupère un service par son identifiant.
     * @param id Identifiant du service recherché.
     * @return Le service correspondant ou null si inexistant.
     */
    public ServiceEntity getById(Long id) {
        return serviceRepository.findById(id).orElse(null);
    }

    /**
     * Supprime un service par son identifiant.
     * @param id Identifiant du service à supprimer.
     */
    public void delete(Long id) {
        serviceRepository.deleteById(id);
    }
}
