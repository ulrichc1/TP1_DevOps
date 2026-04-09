package com.ulrich.dockerjavaspringboot.service;

import com.ulrich.dockerjavaspringboot.model.Produit;
import com.ulrich.dockerjavaspringboot.repository.ProduitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProduitServiceTest {

    @Mock
    private ProduitRepository produitRepository;

    @InjectMocks
    private ProduitService produitService;

    private Produit produit1;
    private Produit produit2;

    @BeforeEach
    public void setUp() {
        produit1 = new Produit("Laptop", "PC portable", 999.99, 10);
        produit1.setId(1L);
        produit2 = new Produit("Souris", "Souris sans fil", 29.99, 50);
        produit2.setId(2L);
    }

    @Test
    public void testListerTous() {
        when(produitRepository.findAll()).thenReturn(Arrays.asList(produit1, produit2));

        List<Produit> result = produitService.listerTous();

        assertEquals(2, result.size());
        verify(produitRepository, times(1)).findAll();
    }

    @Test
    public void testTrouverParId_Trouve() {
        when(produitRepository.findById(1L)).thenReturn(Optional.of(produit1));

        Optional<Produit> result = produitService.trouverParId(1L);

        assertTrue(result.isPresent());
        assertEquals("Laptop", result.get().getNom());
        verify(produitRepository, times(1)).findById(1L);
    }

    @Test
    public void testTrouverParId_NonTrouve() {
        when(produitRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Produit> result = produitService.trouverParId(99L);

        assertFalse(result.isPresent());
    }

    @Test
    public void testEnregistrer() {
        Produit nouveau = new Produit("Clavier", "Clavier mécanique", 79.99, 20);
        when(produitRepository.save(nouveau)).thenReturn(nouveau);

        Produit result = produitService.enregistrer(nouveau);

        assertNotNull(result);
        assertEquals("Clavier", result.getNom());
        verify(produitRepository, times(1)).save(nouveau);
    }

    @Test
    public void testSupprimer() {
        doNothing().when(produitRepository).deleteById(1L);

        produitService.supprimer(1L);

        verify(produitRepository, times(1)).deleteById(1L);
    }
}
