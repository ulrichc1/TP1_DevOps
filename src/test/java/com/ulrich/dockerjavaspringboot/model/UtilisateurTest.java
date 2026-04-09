package com.ulrich.dockerjavaspringboot.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UtilisateurTest {

    @Test
    public void testConstructeur() {
        Utilisateur u = new Utilisateur("Dupont", "Jean", "jean.dupont@email.com");
        assertEquals("Dupont", u.getNom());
        assertEquals("Jean", u.getPrenom());
        assertEquals("jean.dupont@email.com", u.getEmail());
    }

    @Test
    public void testSetNom() {
        Utilisateur u = new Utilisateur("Dupont", "Jean", "jean.dupont@email.com");
        u.setNom("Martin");
        assertEquals("Martin", u.getNom());
    }

    @Test
    public void testSetPrenom() {
        Utilisateur u = new Utilisateur("Dupont", "Jean", "jean.dupont@email.com");
        u.setPrenom("Pierre");
        assertEquals("Pierre", u.getPrenom());
    }

    @Test
    public void testSetEmail() {
        Utilisateur u = new Utilisateur("Dupont", "Jean", "jean.dupont@email.com");
        u.setEmail("pierre.martin@email.com");
        assertEquals("pierre.martin@email.com", u.getEmail());
    }

    @Test
    public void testSetId() {
        Utilisateur u = new Utilisateur("Dupont", "Jean", "jean.dupont@email.com");
        u.setId(7L);
        assertEquals(7L, u.getId());
    }
}
