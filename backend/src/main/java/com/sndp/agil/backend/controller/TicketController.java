package com.sndp.agil.backend.controller;

import com.sndp.agil.backend.model.Ticket;
import com.sndp.agil.backend.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {
    private final TicketService ticketService;

    @PostMapping("/{guichetId}")
    public ResponseEntity<Ticket> creerTicket(
            @PathVariable Long guichetId,
            @RequestParam(required = false, defaultValue = "false") boolean urgent) {
        return ResponseEntity.ok(ticketService.creerTicket(guichetId, urgent));
    }

    @GetMapping("/guichet/{guichetId}")
    public ResponseEntity<List<Ticket>> getFileAttente(
            @PathVariable Long guichetId) {
        return ResponseEntity.ok(ticketService.getTicketsEnAttente(guichetId));
    }
}