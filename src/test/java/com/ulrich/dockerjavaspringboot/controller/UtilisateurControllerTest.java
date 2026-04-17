package com.ulrich.dockerjavaspringboot.controller;

import com.ulrich.dockerjavaspringboot.model.Utilisateur;
import com.ulrich.dockerjavaspringboot.repository.UtilisateurRepository;
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
public class UtilisateurControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testLister() throws Exception {
        mockMvc.perform(get("/utilisateurs"))
                .andExpect(status().isOk())
                .andExpect(view().name("utilisateurs/liste"))
                .andExpect(model().attributeExists("utilisateurs"));
    }

    @Test
    public void testFormulaireCreation() throws Exception {
        mockMvc.perform(get("/utilisateurs/nouveau"))
                .andExpect(status().isOk())
                .andExpect(view().name("utilisateurs/formulaire"))
                .andExpect(model().attributeExists("utilisateur"));
    }

    @Test
    public void testEnregistrerValide() throws Exception {
        mockMvc.perform(post("/utilisateurs/enregistrer")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("nom", "Leroy")
                        .param("prenom", "Anne")
                        .param("email", "anne.leroy@email.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/utilisateurs"));

        Optional<Utilisateur> saved = utilisateurRepository.findAll().stream()
                .filter(u -> "anne.leroy@email.com".equals(u.getEmail()))
                .findFirst();
        assertTrue(saved.isPresent());
    }

    @Test
    public void testEnregistrerInvalide() throws Exception {
        mockMvc.perform(post("/utilisateurs/enregistrer")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("nom", "")
                        .param("prenom", "")
                        .param("email", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("utilisateurs/formulaire"));
    }

    @Test
    public void testFormulaireModificationTrouve() throws Exception {
        Utilisateur utilisateur = utilisateurRepository.save(new Utilisateur("Durand", "Emma", "emma.durand@email.com"));

        mockMvc.perform(get("/utilisateurs/modifier/{id}", utilisateur.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("utilisateurs/formulaire"))
                .andExpect(model().attributeExists("utilisateur"));
    }

    @Test
    public void testFormulaireModificationNonTrouve() throws Exception {
        mockMvc.perform(get("/utilisateurs/modifier/9999"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/utilisateurs"));
    }

    @Test
    public void testSupprimer() throws Exception {
        Utilisateur utilisateur = utilisateurRepository.save(new Utilisateur("Simon", "Leo", "leo.simon@email.com"));

        mockMvc.perform(get("/utilisateurs/supprimer/{id}", utilisateur.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/utilisateurs"));

        assertTrue(utilisateurRepository.findById(utilisateur.getId()).isEmpty());
    }
}
