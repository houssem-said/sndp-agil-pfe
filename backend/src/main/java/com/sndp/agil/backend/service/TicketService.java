package com.sndp.agil.backend.service;

import com.sndp.agil.backend.model.*;
import com.sndp.agil.backend.repository.TicketRepository;
import com.sndp.agil.backend.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;
    private final GuichetService guichetService;
    private final UtilisateurRepository utilisateurRepository; // Ajouté

    @Transactional
    public Ticket creerTicket(Long guichetId, boolean isUrgent) { // Version à 2 paramètres
        Guichet guichet = guichetService.getGuichetById(guichetId);

        // Récupération de l'utilisateur connecté
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Utilisateur utilisateur = utilisateurRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        Ticket ticket = new Ticket();
        ticket.setNumero(genererNumero(isUrgent));
        ticket.setStatut(StatutTicket.EN_ATTENTE);
        ticket.setUrgent(isUrgent);
        ticket.setGuichet(guichet);
        ticket.setUtilisateur(utilisateur); // Assignation de l'utilisateur
        ticket.setDateCreation(LocalDateTime.now());

        return ticketRepository.save(ticket);
    }

    private String genererNumero(boolean isUrgent) {
        return (isUrgent ? "TU-" : "TN-") + System.currentTimeMillis();
    }

    public List<Ticket> getTicketsEnAttente(Long guichetId) {
        return ticketRepository.findByGuichetIdAndStatut(guichetId, StatutTicket.EN_ATTENTE)
                .stream()
                .sorted(Comparator.comparing(Ticket::isUrgent).reversed()
                        .thenComparing(Ticket::getDateCreation))
                .collect(Collectors.toList());
    }
}