/**
 * Ulrich COUDIN - INGE2APP LSI1
 * Date : 06/03/2026
 *
 * Microservice REST - Gestion des Utilisateurs
 */
package com.ulrich.dockerjavaspringboot.controller;

import com.ulrich.dockerjavaspringboot.model.Utilisateur;
import com.ulrich.dockerjavaspringboot.service.UtilisateurService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/utilisateurs")
public class UtilisateurApiController {

    private final UtilisateurService utilisateurService;

    public UtilisateurApiController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @GetMapping
    public ResponseEntity<List<Utilisateur>> listerTous() {
        return ResponseEntity.ok(utilisateurService.listerTous());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Utilisateur> trouverParId(@PathVariable Long id) {
        return utilisateurService.trouverParId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Utilisateur> creer(@Valid @RequestBody Utilisateur utilisateur) {
        Utilisateur nouveau = utilisateurService.enregistrer(utilisateur);
        return ResponseEntity.status(HttpStatus.CREATED).body(nouveau);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Utilisateur> modifier(@PathVariable Long id, @Valid @RequestBody Utilisateur utilisateur) {
        return utilisateurService.trouverParId(id)
                .map(existant -> {
                    utilisateur.setId(id);
                    return ResponseEntity.ok(utilisateurService.enregistrer(utilisateur));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        return utilisateurService.trouverParId(id)
                .map(u -> {
                    utilisateurService.supprimer(id);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
