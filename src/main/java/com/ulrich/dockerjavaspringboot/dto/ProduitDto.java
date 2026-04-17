package com.ulrich.dockerjavaspringboot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProduitDto(
        Long id,
        @NotBlank(message = "Le nom est obligatoire")
        String nom,
        String description,
        @NotNull(message = "Le prix est obligatoire")
        @Positive(message = "Le prix doit etre positif")
        Double prix,
        @NotNull(message = "La quantite est obligatoire")
        @Positive(message = "La quantite doit etre positive")
        Integer quantite
) {
}
