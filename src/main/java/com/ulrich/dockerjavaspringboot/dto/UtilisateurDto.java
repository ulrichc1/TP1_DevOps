package com.ulrich.dockerjavaspringboot.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UtilisateurDto(
        Long id,
        @NotBlank(message = "Le nom est obligatoire")
        String nom,
        @NotBlank(message = "Le prenom est obligatoire")
        String prenom,
        @NotBlank(message = "L'email est obligatoire")
        @Email(message = "L'email doit etre valide")
        String email
) {
}
