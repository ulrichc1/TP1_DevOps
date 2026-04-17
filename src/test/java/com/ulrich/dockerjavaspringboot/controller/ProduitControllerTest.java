package com.ulrich.dockerjavaspringboot.controller;

import com.ulrich.dockerjavaspringboot.model.Produit;
import com.ulrich.dockerjavaspringboot.repository.ProduitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
public class ProduitControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ProduitRepository produitRepository;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testLister() throws Exception {
        mockMvc.perform(get("/produits"))
                .andExpect(status().isOk())
                .andExpect(view().name("produits/liste"))
                .andExpect(model().attributeExists("produits"));
    }

    @Test
    public void testFormulaireCreation() throws Exception {
        mockMvc.perform(get("/produits/nouveau"))
                .andExpect(status().isOk())
                .andExpect(view().name("produits/formulaire"))
                .andExpect(model().attributeExists("produit"));
    }

    @Test
    public void testEnregistrerValide() throws Exception {
        mockMvc.perform(post("/produits/enregistrer")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("nom", "Ecran")
                        .param("description", "Ecran 27 pouces")
                        .param("prix", "199.99")
                        .param("quantite", "7"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/produits"));

        Optional<Produit> saved = produitRepository.findAll().stream()
                .filter(p -> "Ecran".equals(p.getNom()))
                .findFirst();
        assertTrue(saved.isPresent());
    }

    @Test
    public void testEnregistrerInvalide() throws Exception {
        mockMvc.perform(post("/produits/enregistrer")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("nom", "")
                        .param("description", "")
                        .param("prix", "")
                        .param("quantite", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("produits/formulaire"));
    }

    @Test
    public void testFormulaireModificationTrouve() throws Exception {
        Produit produit = produitRepository.save(new Produit("Webcam", "HD", 49.99, 4));

        mockMvc.perform(get("/produits/modifier/{id}", produit.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("produits/formulaire"))
                .andExpect(model().attributeExists("produit"));
    }

    @Test
    public void testFormulaireModificationNonTrouve() throws Exception {
        mockMvc.perform(get("/produits/modifier/9999"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/produits"));
    }

    @Test
    public void testSupprimer() throws Exception {
        Produit produit = produitRepository.save(new Produit("Hub USB", "7 ports", 24.99, 9));

        mockMvc.perform(get("/produits/supprimer/{id}", produit.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/produits"));

        assertTrue(produitRepository.findById(produit.getId()).isEmpty());
    }
}
