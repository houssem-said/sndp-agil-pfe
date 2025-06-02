package com.sndp.agil.backend.service;

import com.sndp.agil.backend.exception.EmptyQueueException;
import com.sndp.agil.backend.model.*;
import com.sndp.agil.backend.repository.TicketRepository;
import com.sndp.agil.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import com.sndp.agil.backend.model.ServiceEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketService {
    private final TicketRepository ticketRepository;
    private final GuichetService guichetService;
    private final UserRepository userRepository;

    public TicketService(TicketRepository ticketRepository,
                         GuichetService guichetService,
                         UserRepository userRepository) {
        this.ticketRepository = ticketRepository;
        this.guichetService = guichetService;
        this.userRepository = userRepository;
    }

    /**
     * Récupère tous les tickets pour un utilisateur donné (par email).
     */
    public List<Ticket> getTicketsByUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User inconnu"));
        return ticketRepository.findByUserId(user.getId());
    }

    /**
     * Crée un nouveau ticket pour un client identifié par son email.
     * On choisit automatiquement un guichet actif (ex : premier guichet du service).
     */
    @Transactional
    public Ticket createTicketForClient(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User inconnu"));

        // Récupérer un guichet actif (par exemple premier guichet actif)
        Guichet guichet = guichetService.getFirstActiveGuichet();
        if (guichet == null) {
            throw new RuntimeException("Aucun guichet actif disponible");
        }

        Ticket ticket = new Ticket();
        ticket.setNumero(generateNumber(false)); // non urgent par défaut
        ticket.setStatut(StatutTicket.EN_ATTENTE);
        ticket.setUrgent(false);
        ticket.setGuichet(guichet);
        ticket.setUser(user);
        ticket.setDateCreation(LocalDateTime.now());

        return ticketRepository.save(ticket);
    }

    /**
     * Récupère la file d’attente (tickets EN_ATTENTE) pour un agent en se basant sur son service.
     */
    public List<Ticket> getTicketsForAgent(String email) {
        User agent = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User inconnu"));

        // Supposons que l’entité User contient un lien vers ServiceEntity service
        ServiceEntity service = agent.getService();
        if (service == null) {
            throw new RuntimeException("Agent non rattaché à un service");
        }

        // Récupérer tous les guichets de ce service
        List<Long> guichetIds = service.getGuichets()
                .stream()
                .map(Guichet::getId)
                .collect(Collectors.toList());

        // Récupérer tous les tickets en attente pour ces guichets
        List<Ticket> queue = ticketRepository.findAll()
                .stream()
                .filter(t -> guichetIds.contains(t.getGuichet().getId()))
                .filter(t -> t.getStatut() == StatutTicket.EN_ATTENTE)
                .collect(Collectors.toList());

        // Tri : d’abord urgents, puis par date de création
        queue.sort(Comparator
                .comparing(Ticket::isUrgent).reversed()
                .thenComparing(Ticket::getDateCreation));

        return queue;
    }

    /**
     * Appelle le prochain ticket pour un agent (change le statut en APPELE).
     * Retourne le ticket appelé.
     */
    @Transactional
    public Ticket callNextTicketForAgent(String email) {
        List<Ticket> queue = getTicketsForAgent(email);
        if (queue.isEmpty()) {
            throw new EmptyQueueException("Pas de ticket en attente");
        }

        Ticket nextTicket = queue.get(0);
        nextTicket.setStatut(StatutTicket.APPELE);
        ticketRepository.save(nextTicket);
        return nextTicket;
    }

    /**
     * Récupère les tickets “APPELE” (en cours) pour l’agent.
     */
    public List<Ticket> getCurrentTickets(String email) {
        User agent = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User inconnu"));

        ServiceEntity service = agent.getService();
        if (service == null) {
            throw new RuntimeException("Agent non rattaché à un service");
        }

        List<Long> guichetIds = service.getGuichets()
                .stream()
                .map(Guichet::getId)
                .collect(Collectors.toList());

        return ticketRepository.findAll()
                .stream()
                .filter(t -> guichetIds.contains(t.getGuichet().getId()))
                .filter(t -> t.getStatut() == StatutTicket.APPELE)
                .collect(Collectors.toList());
    }

    /**
     * Termine un ticket en cours pour l’agent (change le statut en TRAITE et fixe la date de traitement).
     */
    @Transactional
    public void finishTicket(Long ticketId, String email) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket introuvable"));

        // On peut vérifier que ce ticket appartient bien au service de l’agent (optionnel)
        User agent = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User inconnu"));
        ServiceEntity service = agent.getService();
        if (service == null || !service.getGuichets()
                .stream()
                .map(Guichet::getId)
                .collect(Collectors.toList())
                .contains(ticket.getGuichet().getId())) {
            throw new RuntimeException("Accès non autorisé à ce ticket");
        }

        ticket.setStatut(StatutTicket.TRAITE);
        ticket.setDateTraitement(LocalDateTime.now());
        ticketRepository.save(ticket);
    }

    /**
     * Génère un numéro unique pour un ticket (ex : TN-20250602-1234).
     */
    private String generateNumber(boolean isUrgent) {
        String prefix = (isUrgent ? "TU-" : "TN-");
        String timestamp = String.valueOf(System.currentTimeMillis());
        return prefix + timestamp;
    }
}
