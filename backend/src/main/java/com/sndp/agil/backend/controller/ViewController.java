package com.sndp.agil.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // correspond à src/main/resources/templates/login.html
    }

    @GetMapping("/home")
    public String showHomePage() {
        return "home"; // correspond à src/main/resources/templates/home.html
    }
}