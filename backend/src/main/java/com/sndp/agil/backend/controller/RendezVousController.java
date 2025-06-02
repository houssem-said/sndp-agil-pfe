package com.sndp.agil.backend.controller;

import com.sndp.agil.backend.dto.RendezVousDTO;
import com.sndp.agil.backend.model.RendezVous;
import com.sndp.agil.backend.model.User;
import com.sndp.agil.backend.service.RendezVousService;
import com.sndp.agil.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rendezvous")
public class RendezVousController {

    private final RendezVousService rdvService;
    private final UserService userService;

    public RendezVousController(RendezVousService rdvService, UserService userService) {
        this.rdvService = rdvService;
        this.userService = userService;
    }

    /**
     * CLIENT : créer un rendez-vous
     * POST /api/rendezvous/create
     */
    @PostMapping("/create")
    public ResponseEntity<RendezVousDTO> createRdv(
            Authentication authentication,
            @RequestBody RendezVousDTO dto) {

        String email = authentication.getName();
        User user = userService.findByEmail(email);

        // On reconstruit l'objet RendezVous à partir du DTO
        RendezVous rdv = new RendezVous();
        LocalDateTime dateHeure = LocalDateTime.parse(dto.getDate() + "T" + dto.getHeure());
        rdv.setDateHeure(dateHeure);
        rdv.setMotif(dto.getMotif());
        rdv.setService(rdvService.findServiceById(Long.parseLong(dto.getServiceName())));
        // ATTENTION : ici dto.getServiceName() doit contenir en fait l'ID du service
        // (ajustez selon ce que le front envoie).
        // Exemples : dto peut avoir un champ serviceId.

        rdv.setUser(user);
        RendezVous created = rdvService.creerRDV(rdv);
        return ResponseEntity.ok(RendezVousDTO.fromEntity(created));
    }

    /**
     * CLIENT : récupérer ses propres rendez-vous
     * GET /api/rendezvous/my
     */
    @GetMapping("/my")
    public ResponseEntity<List<RendezVousDTO>> getMyRdv(Authentication authentication) {
        String email = authentication.getName();
        User user = userService.findByEmail(email);

        List<RendezVous> list = rdvService.getByUser(user.getId());
        List<RendezVousDTO> dtos = list.stream()
                .map(RendezVousDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    /**
     * OPÉRATEUR : récupérer tous les rendez-vous de l’agence/service
     * GET /api/rendezvous/agency
     */
    @GetMapping("/agency")
    public ResponseEntity<List<RendezVousDTO>> getAllRdvForAgency(Authentication authentication) {
        String email = authentication.getName();
        User agent = userService.findByEmail(email);
        Long serviceId = agent.getService().getId();

        List<RendezVous> list = rdvService.getByServiceId(serviceId);
        List<RendezVousDTO> dtos = list.stream()
                .map(RendezVousDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
}
