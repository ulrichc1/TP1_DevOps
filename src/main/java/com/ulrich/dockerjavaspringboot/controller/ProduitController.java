package com.ulrich.dockerjavaspringboot.controller;

import com.ulrich.dockerjavaspringboot.model.Produit;
import com.ulrich.dockerjavaspringboot.service.ProduitService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/produits")
public class ProduitController {

    private final ProduitService produitService;

    public ProduitController(ProduitService produitService) {
        this.produitService = produitService;
    }

    @GetMapping
    public String lister(Model model) {
        model.addAttribute("produits", produitService.listerTous());
        return "produits/liste";
    }

    @GetMapping("/nouveau")
    public String formulaireCreation(Model model) {
        model.addAttribute("produit", new Produit());
        return "produits/formulaire";
    }

    @PostMapping("/enregistrer")
    public String enregistrer(@Valid @ModelAttribute("produit") Produit produit,
                              BindingResult result,
                              RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "produits/formulaire";
        }
        produitService.enregistrer(produit);
        redirectAttributes.addFlashAttribute("message", "Produit enregistré avec succès !");
        return "redirect:/produits";
    }

    @GetMapping("/modifier/{id}")
    public String formulaireModification(@PathVariable Long id, Model model,
                                         RedirectAttributes redirectAttributes) {
        return produitService.trouverParId(id)
                .map(produit -> {
                    model.addAttribute("produit", produit);
                    return "produits/formulaire";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("erreur", "Produit non trouvé.");
                    return "redirect:/produits";
                });
    }

    @GetMapping("/supprimer/{id}")
    public String supprimer(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        produitService.supprimer(id);
        redirectAttributes.addFlashAttribute("message", "Produit supprimé avec succès !");
        return "redirect:/produits";
    }
}
