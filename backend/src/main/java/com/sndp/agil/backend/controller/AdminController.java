package com.sndp.agil.backend.controller;

import com.sndp.agil.backend.dto.StatsDTO;
import com.sndp.agil.backend.repository.RendezVousRepository;
import com.sndp.agil.backend.repository.TicketRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoints réservés aux administrateurs, par exemple pour obtenir des statistiques globales.
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    private final TicketRepository ticketRepo;
    private final RendezVousRepository rdvRepo;

    public AdminController(TicketRepository ticketRepo, RendezVousRepository rdvRepo) {
        this.ticketRepo = ticketRepo;
        this.rdvRepo = rdvRepo;
    }

    /**
     * Renvoie le nombre total de tickets et de rendez-vous en base.
     * GET /api/admin/stats
     */
    @GetMapping("/stats")
    public StatsDTO getStats() {
        long totalTickets = ticketRepo.count();
        long totalRdv = rdvRepo.count();
        return new StatsDTO(totalTickets, totalRdv);
    }
}
