package com.example.demo.controller;

import com.example.demo.entity.Ticket;
import com.example.demo.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/file-attente") // Point d'entrée pour les requêtes liées à la file d'attente
public class FileAttenteController {

    @Autowired
    private TicketService ticketService; // Service pour gérer les tickets

    /**
     * Récupère des informations sur la file d’attente d’un utilisateur donné
     * @param userId identifiant de l’utilisateur (client)
     * @return Map contenant la position dans la file, temps estimé d’attente, ticket en traitement
     */
    @GetMapping("/user/{userId}")
    public Map<String, Object> getFileAttenteInfo(@PathVariable Long userId) {
        Map<String, Object> response = new HashMap<>();

        List<Ticket> allTickets = ticketService.findAll().stream()
                .filter(t -> !t.isAppele())
                .sorted(Comparator.comparing(Ticket::getDateCreation))
                .collect(Collectors.toList());

        Ticket monTicket = allTickets.stream()
                .filter(t -> t.getUser() != null && t.getUser().getId().equals(userId))
                .findFirst()
                .orElse(null);

        if (monTicket == null) {
            response.put("position", null);
            response.put("tempsEstime", null);
            response.put("enTraitement", null);
            response.put("guichet", null);  // Ajouté ici pour éviter null côté frontend
            return response;
        }

        Ticket enCours = ticketService.findAll().stream()
                .filter(t -> t.isAppele()
                        && t.getGuichet() != null
                        && monTicket.getGuichet() != null
                        && t.getGuichet().getId().equals(monTicket.getGuichet().getId()))
                .max(Comparator.comparing(Ticket::getDateCreation))
                .orElse(null);

        int position = (int) allTickets.stream()
                .takeWhile(t -> !t.getId().equals(monTicket.getId()))
                .filter(t -> t.getGuichet() != null
                        && monTicket.getGuichet() != null
                        && t.getGuichet().getId().equals(monTicket.getGuichet().getId()))
                .count();

        int tempsEstime = position * 5;

        response.put("position", position + 1);
        response.put("tempsEstime", tempsEstime);
        response.put("enTraitement", enCours);
        response.put("guichet", monTicket.getGuichet());  // **Ici on ajoute le guichet**

        return response;
    }
}
