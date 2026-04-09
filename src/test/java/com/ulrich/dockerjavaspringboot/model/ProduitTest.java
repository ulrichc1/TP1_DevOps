package com.ulrich.dockerjavaspringboot.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProduitTest {

    @Test
    public void testConstructeur() {
        Produit produit = new Produit("Laptop", "PC portable", 999.99, 10);
        assertEquals("Laptop", produit.getNom());
        assertEquals("PC portable", produit.getDescription());
        assertEquals(999.99, produit.getPrix());
        assertEquals(10, produit.getQuantite());
    }

    @Test
    public void testSetNom() {
        Produit produit = new Produit("Laptop", "PC portable", 999.99, 10);
        produit.setNom("Tablette");
        assertEquals("Tablette", produit.getNom());
    }

    @Test
    public void testSetDescription() {
        Produit produit = new Produit("Laptop", "PC portable", 999.99, 10);
        produit.setDescription("Tablette tactile");
        assertEquals("Tablette tactile", produit.getDescription());
    }

    @Test
    public void testSetPrix() {
        Produit produit = new Produit("Laptop", "PC portable", 999.99, 10);
        produit.setPrix(1299.99);
        assertEquals(1299.99, produit.getPrix());
    }

    @Test
    public void testSetQuantite() {
        Produit produit = new Produit("Laptop", "PC portable", 999.99, 10);
        produit.setQuantite(25);
        assertEquals(25, produit.getQuantite());
    }

    @Test
    public void testSetId() {
        Produit produit = new Produit("Laptop", "PC portable", 999.99, 10);
        produit.setId(42L);
        assertEquals(42L, produit.getId());
    }
}
