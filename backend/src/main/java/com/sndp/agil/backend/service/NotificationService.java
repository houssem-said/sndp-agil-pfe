package com.sndp.agil.backend.service;

import com.sndp.agil.backend.dto.NotificationDTO;
import com.sndp.agil.backend.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final SimpMessagingTemplate messagingTemplate;
    private final Map<Long, String> lastNotifications = new ConcurrentHashMap<>();

    // Notification pour un ticket appelé
    public void notifyTicketCalled(Ticket ticket) {
        String message = String.format(
                "Ticket #%d - Veuillez vous présenter au guichet %d",
                ticket.getNumero(),
                ticket.getGuichet().getNumero()
        );

        NotificationDTO notification = new NotificationDTO(
                String.valueOf(ticket.getId()),
                message,
                LocalDateTime.now()
        );

        String destination = String.format(
                "/topic/guichet.%d.tickets",
                ticket.getGuichet().getId()
        );

        messagingTemplate.convertAndSend(destination, notification);
        lastNotifications.put(ticket.getGuichet().getId(), message);
    }

    // Notification pour un nouveau ticket
    public void notifyNewTicket(Ticket ticket) {
        // Utilisez getStatut() au lieu d'une hypothétique méthode getPriority()
        String priorityType = (ticket.getStatut() == StatutTicket.URGENT)
                ? "URGENT"
                : "NORMAL";

        NotificationDTO notification = new NotificationDTO(
                String.valueOf(ticket.getId()),
                String.format("Nouveau ticket #%d (%s)",
                        ticket.getNumero(),
                        priorityType),
                ticket.getDateCreation()
        );

        messagingTemplate.convertAndSend("/topic/admin.tickets.new", notification);
    }

    // Notification pour un rendez-vous
    public void notifyRendezVous(RendezVous rdv, String action) {
        String clientName = (rdv.getUtilisateur() != null)
                ? rdv.getUtilisateur().getNom() // Utilisation de getNom() au lieu de getNomComplet()
                : "Client";

        NotificationDTO notification = new NotificationDTO(
                String.valueOf(rdv.getId()),
                String.format("Rendez-vous %s pour %s",
                        action,
                        clientName),
                LocalDateTime.now()
        );

        messagingTemplate.convertAndSend(
                "/topic/rendezvous." + rdv.getStatut().name().toLowerCase(),
                notification
        );
    }

    // Notification système
    public void sendSystemNotification(String message) {
        NotificationDTO notification = new NotificationDTO(
                "system",
                "[SYSTEM] " + message,
                LocalDateTime.now()
        );

        messagingTemplate.convertAndSend(
                "/topic/system.notifications",
                notification
        );
    }

    // Récupère la dernière notification pour un guichet
    public String getLastNotificationForGuichet(Long guichetId) {
        return lastNotifications.getOrDefault(guichetId, "Aucune notification récente");
    }

    // Notification pour un guichet spécifique
    public void notifyGuichet(Long guichetId, String message) {
        NotificationDTO notification = new NotificationDTO(
                "admin",
                "[ADMIN] " + message,
                LocalDateTime.now()
        );
        messagingTemplate.convertAndSend(
                "/topic/guichet." + guichetId + ".admin",
                notification
        );
        lastNotifications.put(guichetId, message);
    }
}