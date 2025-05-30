package com.sndp.agil.backend.controller;

import com.sndp.agil.backend.dto.UserCreateRequest;
import com.sndp.agil.backend.dto.UserDTO;
import com.sndp.agil.backend.model.Utilisateur;
import com.sndp.agil.backend.service.UserService;
import com.sndp.agil.backend.security.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Utilisateur> createUser(
            @Valid @RequestBody UserCreateRequest request
    ) {
        Utilisateur savedUser = userService.createUser(request);
        return ResponseEntity
                .created(URI.create("/api/users/" + savedUser.getId()))
                .body(savedUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Utilisateur> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String email = userDetails.getUsername();
        UserDTO userDTO = userService.findByEmail(email);
        return ResponseEntity.ok(userDTO);
    }
}
