package com.example.demo.controller;

import com.example.demo.entity.Ticket;
import com.example.demo.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

/**
 * Contrôleur REST pour la gestion des tickets.
 * Permet la création, consultation, suppression, et appel du prochain ticket.
 */
@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    /**
     * Crée un nouveau ticket.
     * Initialise la date de création et génère un numéro unique.
     *
     * @param ticket objet ticket envoyé dans la requête (sans date ni numéro)
     * @return ticket sauvegardé avec numéro et date
     */
    @PostMapping
    public Ticket create(@RequestBody Ticket ticket) {
        ticket.setDateCreation(LocalDateTime.now());
        ticket.setNumero(ticketService.generateTicketNumber());
        return ticketService.save(ticket);
    }

    /**
     * Récupère la liste complète des tickets.
     *
     * @return liste de tous les tickets
     */
    @GetMapping
    public List<Ticket> getAll() {
        return ticketService.findAll();
    }

    /**
     * Récupère un ticket par son identifiant.
     *
     * @param id identifiant du ticket
     * @return ticket correspondant
     */
    @GetMapping("/{id}")
    public Ticket getById(@PathVariable Long id) {
        return ticketService.getById(id);
    }

    /**
     * Supprime un ticket par son identifiant.
     *
     * @param id identifiant du ticket à supprimer
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        ticketService.delete(id);
    }

    /**
     * Génère un numéro de ticket unique (sans création de ticket).
     *
     * @return numéro de ticket sous forme de chaîne
     */
    @GetMapping("/generate")
    public String generateTicketNumber() {
        return ticketService.generateTicketNumber();
    }

    /**
     * Appelle (valide) le prochain ticket en attente pour un guichet donné.
     * Le ticket le plus ancien non appelé est sélectionné, son état passé à "appelé".
     *
     * @param guichetId identifiant du guichet
     * @return ticket appelé ou null si aucun ticket en attente
     */
    @PostMapping("/next")
    public ResponseEntity<?> appelerProchain(@RequestParam Long guichetId) {
        List<Ticket> tickets = ticketService.findAll().stream()
                .filter(t -> !t.isAppele() && t.getGuichet() != null && t.getGuichet().getId().equals(guichetId))
                .sorted((t1, t2) -> t1.getDateCreation().compareTo(t2.getDateCreation()))
                .toList();

        if (tickets.isEmpty()) {
            return ResponseEntity.ok().body(null);  // Pas de ticket à appeler
        }

        Ticket prochain = tickets.get(0);
        prochain.setAppele(true);  // Marquer comme appelé
        ticketService.save(prochain);

        return ResponseEntity.ok(prochain);  // Retourne le ticket complet appelé
    }

    /**
     * Récupère tous les tickets associés à un utilisateur donné.
     *
     * @param userId identifiant de l'utilisateur
     * @return liste des tickets liés à l'utilisateur
     */
    @GetMapping("/user/{userId}")
    public List<Ticket> getByUserId(@PathVariable Long userId) {
        return ticketService.findByUserId(userId);
    }

    /**
     * Crée automatiquement un ticket affecté au guichet avec la file d'attente la moins chargée
     * dans une agence donnée (prise de ticket sans rendez-vous).
     *
     * Attend les params userId et agenceId en query string
     * Et dans le body JSON : { "date": "2025-06-28", "heure": "09:30" }
     */
    @PostMapping("/auto")
    public ResponseEntity<?> createTicketAuto(
            @RequestParam Long userId,
            @RequestParam Long agenceId,
            @RequestBody Map<String, String> body) {

        try {
            LocalDate date = LocalDate.parse(body.get("date"));
            LocalTime heure = LocalTime.parse(body.get("heure"));

            Ticket ticket = ticketService.createTicketAuto(userId, agenceId, date, heure);

            if (ticket == null) {
                return ResponseEntity.badRequest().body("Aucun guichet disponible dans cette agence.");
            }
            return ResponseEntity.ok(ticket);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Données invalides pour la date ou l'heure.");
        }
    }

    @GetMapping("/creneaux-pris")
    public ResponseEntity<?> getCreneauxPris(
            @RequestParam Long agenceId,
            @RequestParam String date) {
        try {
            LocalDate localDate = LocalDate.parse(date);
            // Implémentation : récupérer la liste des heures déjà prises ce jour-là dans cette agence
            List<Ticket> tickets = ticketService.findByAgenceIdAndDate(agenceId, localDate);

            // Extraire les heures sous forme de String HH:mm
            List<String> heuresPrises = tickets.stream()
                    .map(t -> t.getDateTicket().toLocalTime().toString().substring(0,5)) // "HH:mm"
                    .toList();

            return ResponseEntity.ok(heuresPrises);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Paramètres invalides");
        }
    }

    @PostMapping("/prise-sans-rdv")
    public ResponseEntity<?> priseSansRdv(
            @RequestParam Long userId,
            @RequestParam Long agenceId,
            @RequestBody Map<String, String> body) {

        try {
            LocalDate date = LocalDate.parse(body.get("date"));
            LocalTime heure = LocalTime.parse(body.get("heure"));

            Ticket ticket = ticketService.createTicketAuto(userId, agenceId, date, heure);

            if (ticket == null) {
                return ResponseEntity.badRequest().body("Aucun guichet disponible dans cette agence.");
            }
            return ResponseEntity.ok(ticket);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Données invalides pour la date ou l'heure.");
        }
    }
}
