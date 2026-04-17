package com.ulrich.dockerjavaspringboot.controller;

import com.ulrich.dockerjavaspringboot.dto.UtilisateurDto;
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
    public ResponseEntity<List<UtilisateurDto>> listerTous() {
        List<UtilisateurDto> utilisateurs = utilisateurService.listerTous().stream()
                .map(this::toDto)
                .toList();
        return ResponseEntity.ok(utilisateurs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UtilisateurDto> trouverParId(@PathVariable Long id) {
        return utilisateurService.trouverParId(id)
                .map(utilisateur -> ResponseEntity.ok(toDto(utilisateur)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UtilisateurDto> creer(@Valid @RequestBody UtilisateurDto utilisateurDto) {
        Utilisateur nouveau = utilisateurService.enregistrer(toEntity(utilisateurDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(nouveau));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UtilisateurDto> modifier(@PathVariable Long id, @Valid @RequestBody UtilisateurDto utilisateurDto) {
        return utilisateurService.trouverParId(id)
                .map(existant -> {
                    Utilisateur aMettreAJour = toEntity(utilisateurDto);
                    aMettreAJour.setId(id);
                    return ResponseEntity.ok(toDto(utilisateurService.enregistrer(aMettreAJour)));
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

    private UtilisateurDto toDto(Utilisateur utilisateur) {
        return new UtilisateurDto(
                utilisateur.getId(),
                utilisateur.getNom(),
                utilisateur.getPrenom(),
                utilisateur.getEmail()
        );
    }

    private Utilisateur toEntity(UtilisateurDto dto) {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(dto.id());
        utilisateur.setNom(dto.nom());
        utilisateur.setPrenom(dto.prenom());
        utilisateur.setEmail(dto.email());
        return utilisateur;
    }
}
