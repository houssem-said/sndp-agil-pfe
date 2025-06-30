package com.example.demo.service;

import com.example.demo.entity.Guichet;
import com.example.demo.entity.Ticket;
import com.example.demo.entity.User;
import com.example.demo.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private GuichetService guichetService;

    @Autowired
    private UserService userService;

    public Ticket save(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    public List<Ticket> findByUserId(Long userId) {
        return ticketRepository.findByUserId(userId);
    }

    public List<Ticket> findAll() {
        return ticketRepository.findAll();
    }

    public Ticket getById(Long id) {
        return ticketRepository.findById(id).orElse(null);
    }

    public void delete(Long id) {
        ticketRepository.deleteById(id);
    }

    public String generateTicketNumber() {
        long count = ticketRepository.count() + 1;
        return "T" + String.format("%04d", count);
    }

    public Ticket createTicketAuto(Long userId, Long agenceId, LocalDate date, LocalTime heure) {
        List<Guichet> guichets = guichetService.findByAgenceId(agenceId);
        if (guichets.isEmpty()) return null;

        Guichet optimalGuichet = guichets.stream()
                .min(Comparator.comparingLong(g -> ticketRepository.countByGuichetIdAndAppeleFalse(g.getId())))
                .orElse(null);

        if (optimalGuichet == null) return null;

        User user = userService.getById(userId);
        if (user == null) return null;

        Ticket ticket = new Ticket();
        ticket.setDateTicket(date.atTime(heure));
        ticket.setDateCreation(LocalDateTime.now());
        ticket.setNumero(generateTicketNumber());
        ticket.setAppele(false);
        ticket.setUser(user);
        ticket.setGuichet(optimalGuichet);
        ticket.setAgence(optimalGuichet.getAgence());

        return ticketRepository.save(ticket);
    }

    public List<Ticket> findByAgenceIdAndDate(Long agenceId, LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(23, 59, 59);
        return ticketRepository.findByAgenceIdAndDateTicketBetween(agenceId, start, end);
    }
}
