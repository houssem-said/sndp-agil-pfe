package com.sndp.agil.backend.dto;

import com.sndp.agil.backend.model.RoleUtilisateur;
import jakarta.validation.constraints.*;

public record UserCreateRequest(
        @NotBlank(message = "L'email est obligatoire")
        @Email(message = "Format d'email invalide")
        String email,

        @NotBlank(message = "Le mot de passe est obligatoire")
        @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$",
                message = "Le mot de passe doit contenir au moins 1 majuscule, 1 minuscule et 1 chiffre"
        )
        String password,

        @NotBlank(message = "Le nom est obligatoire")
        String username,  // ajouté pour le nom complet

        @NotNull(message = "Le rôle est obligatoire")
        RoleUtilisateur role
) {}
