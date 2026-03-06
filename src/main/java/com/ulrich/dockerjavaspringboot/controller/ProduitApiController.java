/**
 * Ulrich COUDIN - INGE2APP LSI1
 * Date : 06/03/2026
 *
 * Microservice REST - Gestion des Produits
 */
package com.ulrich.dockerjavaspringboot.controller;

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
    public ResponseEntity<List<Produit>> listerTous() {
        return ResponseEntity.ok(produitService.listerTous());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produit> trouverParId(@PathVariable Long id) {
        return produitService.trouverParId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Produit> creer(@Valid @RequestBody Produit produit) {
        Produit nouveau = produitService.enregistrer(produit);
        return ResponseEntity.status(HttpStatus.CREATED).body(nouveau);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Produit> modifier(@PathVariable Long id, @Valid @RequestBody Produit produit) {
        return produitService.trouverParId(id)
                .map(existant -> {
                    produit.setId(id);
                    return ResponseEntity.ok(produitService.enregistrer(produit));
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
}
