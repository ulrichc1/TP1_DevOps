package com.ulrich.dockerjavaspringboot.service;

import com.ulrich.dockerjavaspringboot.model.Produit;
import com.ulrich.dockerjavaspringboot.repository.ProduitRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProduitService {

    private final ProduitRepository produitRepository;

    public ProduitService(ProduitRepository produitRepository) {
        this.produitRepository = produitRepository;
    }

    public List<Produit> listerTous() {
        return produitRepository.findAll();
    }

    public Optional<Produit> trouverParId(Long id) {
        return produitRepository.findById(id);
    }

    public Produit enregistrer(Produit produit) {
        return produitRepository.save(produit);
    }

    public void supprimer(Long id) {
        produitRepository.deleteById(id);
    }
}
