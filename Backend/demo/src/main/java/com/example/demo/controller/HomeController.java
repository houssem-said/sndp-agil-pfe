package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur simple pour vérifier que le backend fonctionne correctement.
 * Expose une route GET "/" qui renvoie un message de confirmation.
 */
@RestController
public class HomeController {

    /**
     * Point d'entrée racine du backend.
     * @return message de confirmation que le backend est opérationnel.
     */
    @GetMapping("/")
    public String home() {
        return "✅ Backend SNDP Agil est opérationnel";
    }
}
