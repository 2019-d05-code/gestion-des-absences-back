package dev.services;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dev.controller.vm.DemandeAbsenceDTO;
import dev.domain.Collegue;
import dev.domain.enums.Type;
import dev.exceptions.DemandeInvalideException;
import dev.repository.CollegueRepo;

public class DemandeAbsenceServiceTest {
	
	private static final Logger LOG = LoggerFactory.getLogger(DemandeAbsenceService.class);

	DemandeAbsenceService service;
	
	private CollegueRepo crMock;
	
	@Before
	public void init() {
		this.service = new DemandeAbsenceService();
		this.crMock = Mockito.mock(CollegueRepo.class);
		this.service.setCollegueRepo(this.crMock);
	}
	
	// Tests relatif à l'ajout d'une nouvelle demande
	
	@Test(expected = DemandeInvalideException.class)
	public void testSauvegarderDemandeAbsenceSansMotif() {
		
		LOG.info("Etant donné une demande de type congé sans solde sans motif");
		DemandeAbsenceDTO demande = new DemandeAbsenceDTO();
		demande.setEmail("admin@dev.fr");
		demande.setDateDebut(LocalDate.now().plusDays(1));
		demande.setDateFin(LocalDate.now().plusDays(5));
		demande.setType(Type.CONGES_SANS_SOLDE);
		
		Collegue collegue = new Collegue();
		
		Mockito.when(crMock.findByEmail(demande.getEmail())).thenReturn(Optional.of(collegue));
		
		LOG.info("Lorsqu'on tente de sauvegarder cette demande");
		LOG.info("Alors une exception est renvoyée");
		
		service.enregistrerDemandeAbsence(demande);
	}
	
	@Test(expected = DemandeInvalideException.class)
	public void testSauvegarderDemandeAbsenceAvecDateFinInvalide() {
		
		LOG.info("Etant donné une demande avec une date de fin invalide");
		DemandeAbsenceDTO demande = new DemandeAbsenceDTO();
		demande.setEmail("admin@dev.fr");
		demande.setDateDebut(LocalDate.now().plusDays(6));
		demande.setDateFin(LocalDate.now().plusDays(5));
		demande.setType(Type.CONGES_PAYES);
		
		Collegue collegue = new Collegue();
		
		Mockito.when(crMock.findByEmail(demande.getEmail())).thenReturn(Optional.of(collegue));
		
		LOG.info("Lorsqu'on tente de sauvegarder cette demande");
		LOG.info("Alors une exception est renvoyée");
		
		service.enregistrerDemandeAbsence(demande);
	}
	
	@Test(expected = DemandeInvalideException.class)
	public void testSauvegarderDemandeAbsenceAvecDateDebutInvalide() {
		
		LOG.info("Etant donné une demande avec une date de début invalide");
		DemandeAbsenceDTO demande = new DemandeAbsenceDTO();
		demande.setEmail("admin@dev.fr");
		demande.setDateDebut(LocalDate.now());
		demande.setDateFin(LocalDate.now().plusDays(5));
		demande.setType(Type.CONGES_PAYES);
		
		Collegue collegue = new Collegue();
		
		Mockito.when(crMock.findByEmail(demande.getEmail())).thenReturn(Optional.of(collegue));
		
		LOG.info("Lorsqu'on tente de sauvegarder cette demande");
		LOG.info("Alors une exception est renvoyée");
		
		service.enregistrerDemandeAbsence(demande);
	}

	
}
