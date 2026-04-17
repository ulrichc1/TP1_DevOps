package com.ulrich.dockerjavaspringboot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ulrich.dockerjavaspringboot.dto.ProduitDto;
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
public class ProduitApiControllerTest {

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
        mockMvc.perform(get("/api/produits"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testCreerProduit() throws Exception {
        ProduitDto produit = new ProduitDto(null, "Laptop", "PC portable", 999.99, 10);

        mockMvc.perform(post("/api/produits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(produit)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testCreerProduit_DonneesInvalides() throws Exception {
        ProduitDto invalide = new ProduitDto(null, "", "", -1.0, -5);

        mockMvc.perform(post("/api/produits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalide)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testTrouverParId_NonTrouve() throws Exception {
        mockMvc.perform(get("/api/produits/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreerPuisRecuperer() throws Exception {
        ProduitDto produit = new ProduitDto(null, "Souris", "Souris sans fil", 29.99, 50);

        String response = mockMvc.perform(post("/api/produits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(produit)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(get("/api/produits/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("Souris"))
                .andExpect(jsonPath("$.prix").value(29.99));
    }

    @Test
    public void testSupprimerProduit() throws Exception {
        ProduitDto produit = new ProduitDto(null, "Clavier", "Clavier mecanique", 79.99, 20);

        String response = mockMvc.perform(post("/api/produits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(produit)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(delete("/api/produits/" + id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/produits/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testModifierProduit() throws Exception {
        ProduitDto produit = new ProduitDto(null, "Casque", "Casque audio", 59.99, 15);

        String response = mockMvc.perform(post("/api/produits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(produit)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        ProduitDto modifie = new ProduitDto(null, "Casque Pro", "Casque audio haute fidelite", 89.99, 12);

        mockMvc.perform(put("/api/produits/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modifie)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.nom").value("Casque Pro"))
                .andExpect(jsonPath("$.prix").value(89.99));
    }

    @Test
    public void testModifierProduit_NonTrouve() throws Exception {
        ProduitDto modifie = new ProduitDto(null, "Inexistant", "Produit", 10.0, 1);

        mockMvc.perform(put("/api/produits/9999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modifie)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testSupprimerProduit_NonTrouve() throws Exception {
        mockMvc.perform(delete("/api/produits/9999"))
                .andExpect(status().isNotFound());
    }
}
