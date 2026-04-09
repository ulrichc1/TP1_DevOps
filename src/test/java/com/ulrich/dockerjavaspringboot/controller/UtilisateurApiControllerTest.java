package com.ulrich.dockerjavaspringboot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ulrich.dockerjavaspringboot.model.Utilisateur;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class UtilisateurApiControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testListerTous() throws Exception {
        mockMvc.perform(get("/api/utilisateurs"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testCreerUtilisateur() throws Exception {
        Utilisateur u = new Utilisateur("Dupont", "Jean", "jean.dupont@email.com");

        mockMvc.perform(post("/api/utilisateurs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(u)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testCreerUtilisateur_EmailInvalide() throws Exception {
        Utilisateur invalide = new Utilisateur("Dupont", "Jean", "pas-un-email");

        mockMvc.perform(post("/api/utilisateurs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalide)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testTrouverParId_NonTrouve() throws Exception {
        mockMvc.perform(get("/api/utilisateurs/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreerPuisRecuperer() throws Exception {
        Utilisateur u = new Utilisateur("Martin", "Claire", "claire.martin@email.com");

        String response = mockMvc.perform(post("/api/utilisateurs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(u)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(get("/api/utilisateurs/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("Martin"))
                .andExpect(jsonPath("$.email").value("claire.martin@email.com"));
    }
}
