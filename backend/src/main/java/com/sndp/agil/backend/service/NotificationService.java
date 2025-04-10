package com.sndp.agil.backend.service;

import com.sndp.agil.backend.dto.NotificationDTO;
import com.sndp.agil.backend.model.RendezVous;
import com.sndp.agil.backend.model.Ticket;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class NotificationService {
    private final SimpMessagingTemplate messagingTemplate;

    public NotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void notifyTicketAppele(Ticket ticket) {
        messagingTemplate.convertAndSend(
                "/topic/guichet." + ticket.getGuichet().getId(),
                new NotificationDTO(
                        ticket.getNumero(),
                        "Veuillez vous présenter au guichet " + ticket.getGuichet().getNumero(),
                        LocalDateTime.now()
                )
        );
    }

    public void notifyTicketCreated(Ticket ticket) {
        messagingTemplate.convertAndSend(
                "/topic/guichet." + ticket.getGuichet().getId() + ".new",
                new NotificationDTO(
                        ticket.getNumero(),
                        "Nouveau ticket créé",
                        ticket.getDateCreation()
                )
        );
    }
}