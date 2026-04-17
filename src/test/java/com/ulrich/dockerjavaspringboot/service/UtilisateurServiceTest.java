package com.ulrich.dockerjavaspringboot.service;

import com.ulrich.dockerjavaspringboot.model.Utilisateur;
import com.ulrich.dockerjavaspringboot.repository.UtilisateurRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UtilisateurServiceTest {

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @InjectMocks
    private UtilisateurService utilisateurService;

    private Utilisateur utilisateur1;
    private Utilisateur utilisateur2;

    @BeforeEach
    public void setUp() {
        utilisateur1 = new Utilisateur("Dupont", "Jean", "jean.dupont@email.com");
        utilisateur1.setId(1L);
        utilisateur2 = new Utilisateur("Martin", "Claire", "claire.martin@email.com");
        utilisateur2.setId(2L);
    }

    @Test
    public void testListerTous() {
        when(utilisateurRepository.findAll()).thenReturn(Arrays.asList(utilisateur1, utilisateur2));

        List<Utilisateur> result = utilisateurService.listerTous();

        assertEquals(2, result.size());
        verify(utilisateurRepository, times(1)).findAll();
    }

    @Test
    public void testTrouverParId_Trouve() {
        when(utilisateurRepository.findById(1L)).thenReturn(Optional.of(utilisateur1));

        Optional<Utilisateur> result = utilisateurService.trouverParId(1L);

        assertTrue(result.isPresent());
        assertEquals("Dupont", result.get().getNom());
        verify(utilisateurRepository, times(1)).findById(1L);
    }

    @Test
    public void testTrouverParId_NonTrouve() {
        when(utilisateurRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Utilisateur> result = utilisateurService.trouverParId(99L);

        assertFalse(result.isPresent());
    }

    @Test
    public void testEnregistrer() {
        Utilisateur nouveau = new Utilisateur("Bernard", "Luc", "luc.bernard@email.com");
        when(utilisateurRepository.save(nouveau)).thenReturn(nouveau);

        Utilisateur result = utilisateurService.enregistrer(nouveau);

        assertNotNull(result);
        assertEquals("Bernard", result.getNom());
        verify(utilisateurRepository, times(1)).save(nouveau);
    }

    @Test
    public void testSupprimer() {
        doNothing().when(utilisateurRepository).deleteById(1L);

        utilisateurService.supprimer(1L);

        verify(utilisateurRepository, times(1)).deleteById(1L);
    }
}
