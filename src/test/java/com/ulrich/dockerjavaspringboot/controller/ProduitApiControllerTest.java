package com.ulrich.dockerjavaspringboot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ulrich.dockerjavaspringboot.model.Produit;
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
        Produit produit = new Produit("Laptop", "PC portable", 999.99, 10);

        mockMvc.perform(post("/api/produits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(produit)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testCreerProduit_DonneesInvalides() throws Exception {
        Produit invalide = new Produit("", "", -1.0, -5);

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
        Produit produit = new Produit("Souris", "Souris sans fil", 29.99, 50);

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
        Produit produit = new Produit("Clavier", "Clavier mécanique", 79.99, 20);

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
}
