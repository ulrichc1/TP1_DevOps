package com.ulrich.dockerjavaspringboot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ulrich.dockerjavaspringboot.dto.UtilisateurDto;
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
        UtilisateurDto u = new UtilisateurDto(null, "Dupont", "Jean", "jean.dupont@email.com");

        mockMvc.perform(post("/api/utilisateurs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(u)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testCreerUtilisateur_EmailInvalide() throws Exception {
        UtilisateurDto invalide = new UtilisateurDto(null, "Dupont", "Jean", "pas-un-email");

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
        UtilisateurDto u = new UtilisateurDto(null, "Martin", "Claire", "claire.martin@email.com");

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

    @Test
    public void testModifierUtilisateur() throws Exception {
        UtilisateurDto u = new UtilisateurDto(null, "Durand", "Alice", "alice.durand@email.com");

        String response = mockMvc.perform(post("/api/utilisateurs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(u)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        UtilisateurDto modifie = new UtilisateurDto(null, "Durand", "Alicia", "alicia.durand@email.com");

        mockMvc.perform(put("/api/utilisateurs/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modifie)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.prenom").value("Alicia"))
                .andExpect(jsonPath("$.email").value("alicia.durand@email.com"));
    }

    @Test
    public void testModifierUtilisateur_NonTrouve() throws Exception {
        UtilisateurDto modifie = new UtilisateurDto(null, "Inexistant", "User", "user@email.com");

        mockMvc.perform(put("/api/utilisateurs/9999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modifie)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testSupprimerUtilisateur() throws Exception {
        UtilisateurDto u = new UtilisateurDto(null, "Petit", "Marc", "marc.petit@email.com");

        String response = mockMvc.perform(post("/api/utilisateurs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(u)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(delete("/api/utilisateurs/" + id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/utilisateurs/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testSupprimerUtilisateur_NonTrouve() throws Exception {
        mockMvc.perform(delete("/api/utilisateurs/9999"))
                .andExpect(status().isNotFound());
    }
}
