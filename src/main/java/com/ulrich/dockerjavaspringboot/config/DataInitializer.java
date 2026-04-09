package com.ulrich.dockerjavaspringboot.config;

import com.ulrich.dockerjavaspringboot.model.Produit;
import com.ulrich.dockerjavaspringboot.model.Utilisateur;
import com.ulrich.dockerjavaspringboot.repository.ProduitRepository;
import com.ulrich.dockerjavaspringboot.repository.UtilisateurRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ProduitRepository produitRepository;
    private final UtilisateurRepository utilisateurRepository;

    public DataInitializer(ProduitRepository produitRepository, UtilisateurRepository utilisateurRepository) {
        this.produitRepository = produitRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    @Override
    public void run(String... args) {
        produitRepository.save(new Produit("Ordinateur portable", "PC portable 15 pouces", 899.99, 10));
        produitRepository.save(new Produit("Clavier mécanique", "Clavier RGB switches bleus", 79.99, 25));
        produitRepository.save(new Produit("Souris sans fil", "Souris ergonomique Bluetooth", 34.99, 50));

        utilisateurRepository.save(new Utilisateur("Martin", "Pierre", "pierre.martin@exemple.com"));
        utilisateurRepository.save(new Utilisateur("Dupont", "Jean", "jean.dupont@exemple.com"));
    }
}
