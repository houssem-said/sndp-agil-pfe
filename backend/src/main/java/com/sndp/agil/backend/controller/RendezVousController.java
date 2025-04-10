package com.sndp.agil.backend.controller;

import com.sndp.agil.backend.model.RendezVous;
import com.sndp.agil.backend.service.RendezVousService;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/rdv")
public class RendezVousController {
    private final RendezVousService rendezVousService;

    public RendezVousController(RendezVousService rendezVousService) {
        this.rendezVousService = rendezVousService;
    }

    @PostMapping
    public RendezVous creerRDV(@RequestBody RendezVous rdv) {
        return rendezVousService.creerRDV(rdv);
    }

    @GetMapping("/jour")
    public List<RendezVous> getRDVDuJour(@RequestParam String date) {
        LocalDateTime dateDebut = LocalDateTime.parse(date + "T00:00:00");
        return rendezVousService.getRDVParJour(dateDebut);
    }
}