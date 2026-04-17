package com.ulrich.dockerjavaspringboot.controller;

import com.ulrich.dockerjavaspringboot.model.Utilisateur;
import com.ulrich.dockerjavaspringboot.service.UtilisateurService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/utilisateurs")
public class UtilisateurController {

    private static final String FORM_VIEW = "utilisateurs/formulaire";
    private static final String REDIRECT_LIST = "redirect:/utilisateurs";

    private final UtilisateurService utilisateurService;

    public UtilisateurController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @GetMapping
    public String lister(Model model) {
        model.addAttribute("utilisateurs", utilisateurService.listerTous());
        return "utilisateurs/liste";
    }

    @GetMapping("/nouveau")
    public String formulaireCreation(Model model) {
        model.addAttribute("utilisateur", new Utilisateur());
        return FORM_VIEW;
    }

    @PostMapping("/enregistrer")
    public String enregistrer(@Valid @ModelAttribute("utilisateur") Utilisateur utilisateur,
                              BindingResult result,
                              RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return FORM_VIEW;
        }
        utilisateurService.enregistrer(utilisateur);
        redirectAttributes.addFlashAttribute("message", "Utilisateur enregistré avec succès !");
        return REDIRECT_LIST;
    }

    @GetMapping("/modifier/{id}")
    public String formulaireModification(@PathVariable Long id, Model model,
                                         RedirectAttributes redirectAttributes) {
        return utilisateurService.trouverParId(id)
                .map(utilisateur -> {
                    model.addAttribute("utilisateur", utilisateur);
                    return FORM_VIEW;
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("erreur", "Utilisateur non trouvé.");
                    return REDIRECT_LIST;
                });
    }

    @GetMapping("/supprimer/{id}")
    public String supprimer(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        utilisateurService.supprimer(id);
        redirectAttributes.addFlashAttribute("message", "Utilisateur supprimé avec succès !");
        return REDIRECT_LIST;
    }
}
