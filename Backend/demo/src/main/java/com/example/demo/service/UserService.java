package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service dédié à la gestion des utilisateurs.
 *
 * Cette classe fait l'intermédiaire entre le contrôleur et le repository,
 * fournissant les opérations CRUD et les requêtes spécifiques sur les utilisateurs.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Récupère la liste de tous les utilisateurs.
     * @return Liste complète des utilisateurs.
     */
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * Récupère un utilisateur par son identifiant,
     * en chargeant aussi son guichet et ses tickets associés (fetch eager).
     * @param id Identifiant de l'utilisateur.
     * @return Utilisateur ou null s'il n'existe pas.
     */
    public User getById(Long id) {
        return userRepository.findByIdWithGuichetAndTickets(id).orElse(null);
    }

    /**
     * Sauvegarde un utilisateur (création ou mise à jour).
     * @param user Utilisateur à sauvegarder.
     * @return Utilisateur sauvegardé.
     */
    public User save(User user) {
        return userRepository.save(user);
    }

    /**
     * Supprime un utilisateur par son identifiant.
     * @param id Identifiant de l'utilisateur à supprimer.
     * @return true si l'utilisateur existait et a été supprimé, false sinon.
     */
    public boolean delete(Long id) {
        if (!userRepository.existsById(id)) {
            return false;
        }
        userRepository.deleteById(id);
        return true;
    }

    /**
     * Recherche un utilisateur par son adresse email.
     * @param email Adresse email.
     * @return Utilisateur trouvé ou null si aucun résultat.
     */
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    /**
     * Récupère la liste des utilisateurs ayant un rôle spécifique.
     * Exemple de rôles : "USER", "OPERATEUR", "ADMIN".
     * @param role Rôle à filtrer.
     * @return Liste des utilisateurs avec ce rôle.
     */
    public List<User> findByRole(String role) {
        return userRepository.findByRole(role);
    }
}
