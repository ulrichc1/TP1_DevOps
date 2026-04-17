package com.ulrich.dockerjavaspringboot.controller;

import com.ulrich.dockerjavaspringboot.dto.ProduitDto;
import com.ulrich.dockerjavaspringboot.model.Produit;
import com.ulrich.dockerjavaspringboot.service.ProduitService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produits")
public class ProduitApiController {

    private final ProduitService produitService;

    public ProduitApiController(ProduitService produitService) {
        this.produitService = produitService;
    }

    @GetMapping
    public ResponseEntity<List<ProduitDto>> listerTous() {
        List<ProduitDto> produits = produitService.listerTous().stream()
                .map(this::toDto)
                .toList();
        return ResponseEntity.ok(produits);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProduitDto> trouverParId(@PathVariable Long id) {
        return produitService.trouverParId(id)
                .map(produit -> ResponseEntity.ok(toDto(produit)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProduitDto> creer(@Valid @RequestBody ProduitDto produitDto) {
        Produit nouveau = produitService.enregistrer(toEntity(produitDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(nouveau));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProduitDto> modifier(@PathVariable Long id, @Valid @RequestBody ProduitDto produitDto) {
        return produitService.trouverParId(id)
                .map(existant -> {
                    Produit aMettreAJour = toEntity(produitDto);
                    aMettreAJour.setId(id);
                    return ResponseEntity.ok(toDto(produitService.enregistrer(aMettreAJour)));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        return produitService.trouverParId(id)
                .map(produit -> {
                    produitService.supprimer(id);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    private ProduitDto toDto(Produit produit) {
        return new ProduitDto(
                produit.getId(),
                produit.getNom(),
                produit.getDescription(),
                produit.getPrix(),
                produit.getQuantite()
        );
    }

    private Produit toEntity(ProduitDto dto) {
        Produit produit = new Produit();
        produit.setId(dto.id());
        produit.setNom(dto.nom());
        produit.setDescription(dto.description());
        produit.setPrix(dto.prix());
        produit.setQuantite(dto.quantite());
        return produit;
    }
}
