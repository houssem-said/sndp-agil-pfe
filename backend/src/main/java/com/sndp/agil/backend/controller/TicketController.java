package com.sndp.agil.backend.controller;

import com.sndp.agil.backend.dto.TicketDTO;
import com.sndp.agil.backend.exception.EmptyQueueException;
import com.sndp.agil.backend.model.Ticket;
import com.sndp.agil.backend.security.UserDetailsImpl;
import com.sndp.agil.backend.service.TicketService;
import com.sndp.agil.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;
    private final UserService userService;

    public TicketController(TicketService ticketService, UserService userService) {
        this.ticketService = ticketService;
        this.userService = userService;
    }

    /**
     * CLIENT : récupérer tous ses tickets
     * GET /api/tickets/my
     */
    @GetMapping("/my")
    public ResponseEntity<List<TicketDTO>> getMyTickets(Authentication authentication) {
        String email = authentication.getName();
        List<Ticket> tickets = ticketService.getTicketsByUser(email);
        List<TicketDTO> dtos = tickets.stream()
                .map(TicketDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    /**
     * CLIENT : créer un nouveau ticket pour l'utilisateur connecté
     * POST /api/tickets
     */
    @PostMapping
    public ResponseEntity<TicketDTO> createMyTicket(Authentication authentication) {
        String email = authentication.getName();
        Ticket created = ticketService.createTicketForClient(email);
        return ResponseEntity.ok(TicketDTO.fromEntity(created));
    }

    /**
     * OPÉRATEUR : récupérer la file d’attente (tickets EN_ATTENTE) pour l’agence/service de l’agent
     * GET /api/tickets/agency
     */
    @GetMapping("/agency")
    public ResponseEntity<List<TicketDTO>> getTicketsForAgency(Authentication authentication) {
        String email = authentication.getName();
        List<Ticket> tickets = ticketService.getTicketsForAgent(email);
        List<TicketDTO> dtos = tickets.stream()
                .map(TicketDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    /**
     * OPÉRATEUR : appeler le prochain ticket (change le statut en APPELE)
     * POST /api/tickets/call-next
     */
    @PostMapping("/call-next")
    public ResponseEntity<TicketDTO> callNextTicket(Authentication authentication) {
        String email = authentication.getName();
        Ticket nextTicket;
        try {
            nextTicket = ticketService.callNextTicketForAgent(email);
        } catch (EmptyQueueException e) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(TicketDTO.fromEntity(nextTicket));
    }

    /**
     * OPÉRATEUR : récupérer les tickets “APPELE” (en cours) pour l’agent
     * GET /api/tickets/current
     */
    @GetMapping("/current")
    public ResponseEntity<List<TicketDTO>> getCurrentTicketsForAgent(Authentication authentication) {
        String email = authentication.getName();
        List<Ticket> current = ticketService.getCurrentTickets(email);
        List<TicketDTO> dtos = current.stream()
                .map(TicketDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    /**
     * OPÉRATEUR : terminer un ticket en cours
     * POST /api/tickets/{ticketId}/finish
     */
    @PostMapping("/{ticketId}/finish")
    public ResponseEntity<Void> finishTicket(@PathVariable Long ticketId,
                                             Authentication authentication) {
        String email = authentication.getName();
        ticketService.finishTicket(ticketId, email);
        return ResponseEntity.ok().build();
    }
}
