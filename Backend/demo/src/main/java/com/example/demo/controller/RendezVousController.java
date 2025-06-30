package com.example.demo.controller;

import com.example.demo.dto.RendezVousRequest;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.service.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/rendezvous")
public class RendezVousController {

    @Autowired
    private RendezVousService rendezVousService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AgenceRepository agenceRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private GuichetRepository guichetRepository;

    @Autowired
    private TicketService ticketService;

    // Accepte des heures au format H:mm ou HH:mm
    private static final DateTimeFormatter TIME_FORMATTER = new DateTimeFormatterBuilder()
            .appendPattern("[H:mm][HH:mm]")
            .toFormatter();

    @PostMapping
    public ResponseEntity<RendezVous> create(@RequestBody RendezVousRequest request) {
        User user = userRepository.findById(request.getUserId()).orElse(null);
        Agence agence = agenceRepository.findById(request.getAgenceId()).orElse(null);
        ServiceEntity service = serviceRepository.findById(request.getServiceId()).orElse(null);

        if (user == null || agence == null || service == null) {
            return ResponseEntity.badRequest().build();
        }

        LocalDate date = LocalDate.parse(request.getDate());
        LocalTime heure = LocalTime.parse(request.getHeure(), TIME_FORMATTER);

        // Générer automatiquement un ticket et affecter le guichet
        Ticket ticket = ticketService.createTicketAuto(user.getId(), agence.getId(), date, heure);
        Guichet guichet = (ticket != null) ? ticket.getGuichet() : null;

        RendezVous rdv = new RendezVous();
        rdv.setUser(user);
        rdv.setAgence(agence);
        rdv.setService(service);
        rdv.setGuichet(guichet);
        rdv.setDate(date);
        rdv.setHeure(heure);

        RendezVous saved = rendezVousService.save(rdv);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RendezVous> update(@PathVariable Long id, @RequestBody RendezVousRequest request) {
        RendezVous rdv = rendezVousService.getById(id);
        if (rdv == null) return ResponseEntity.notFound().build();

        User user = userRepository.findById(request.getUserId()).orElse(null);
        Agence agence = agenceRepository.findById(request.getAgenceId()).orElse(null);
        ServiceEntity service = serviceRepository.findById(request.getServiceId()).orElse(null);

        if (user == null || agence == null || service == null) {
            return ResponseEntity.badRequest().build();
        }

        LocalDate date = LocalDate.parse(request.getDate());
        LocalTime heure = LocalTime.parse(request.getHeure(), TIME_FORMATTER);

        // Réaffecter le guichet si nécessaire (optionnel)
        List<Guichet> guichets = guichetRepository.findOpenGuichetsOrderByTicketsCountAsc(agence.getId());
        Guichet guichet = guichets.isEmpty() ? null : guichets.get(0);

        rdv.setUser(user);
        rdv.setAgence(agence);
        rdv.setService(service);
        rdv.setGuichet(guichet);
        rdv.setDate(date);
        rdv.setHeure(heure);

        RendezVous updated = rendezVousService.save(rdv);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        RendezVous rdv = rendezVousService.getById(id);
        if (rdv == null) return ResponseEntity.notFound().build();
        rendezVousService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public List<RendezVous> getAll() {
        return rendezVousService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RendezVous> getById(@PathVariable Long id) {
        RendezVous rdv = rendezVousService.getById(id);
        return (rdv != null) ? ResponseEntity.ok(rdv) : ResponseEntity.notFound().build();
    }

    @GetMapping("/user/{userId}")
    public List<RendezVous> getByUserId(@PathVariable Long userId) {
        return rendezVousService.findByUserId(userId);
    }

    @GetMapping("/user/{userId}/future")
    public List<RendezVous> getFutursByUserId(@PathVariable Long userId) {
        return rendezVousService.findFutureByUserId(userId);
    }

    @GetMapping("/creneaux-pris")
    public List<String> getCreneauxPris(
            @RequestParam Long agenceId,
            @RequestParam Long serviceId,
            @RequestParam String date) {
        return rendezVousService.getCreneauxPris(agenceId, serviceId, date);
    }
}