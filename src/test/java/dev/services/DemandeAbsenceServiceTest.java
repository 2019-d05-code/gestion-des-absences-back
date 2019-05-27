package dev.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dev.controller.vm.DemandeAbsenceDTO;
import dev.domain.Collegue;
import dev.domain.DemandeAbsence;
import dev.domain.enums.Status;
import dev.domain.enums.Type;
import dev.exceptions.DemandeInvalideException;
import dev.exceptions.DemandeNonTrouveException;
import dev.exceptions.ModificationInvalideException;
import dev.repository.CollegueRepo;
import dev.repository.DemandeAbsenceRepo;

public class DemandeAbsenceServiceTest {
	
	private static final Logger LOG = LoggerFactory.getLogger(DemandeAbsenceService.class);

	DemandeAbsenceService service;
	
	private CollegueRepo crMock;
	
	private DemandeAbsenceRepo drMock;
	
	@Before
	public void init() {
		this.service = new DemandeAbsenceService();
		this.crMock = Mockito.mock(CollegueRepo.class);
		this.service.setCollegueRepo(this.crMock);
		this.drMock = Mockito.mock(DemandeAbsenceRepo.class);
		this.service.setDemandeRepo(this.drMock);
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
	
	@Test(expected = DemandeInvalideException.class)
	public void testSauvegarderDemandeAbsenceAvecPeriodeChevauchantPeriodeAutreCollegue() {
		
		LOG.info("Etant donné une première demande valide et une deuxième demande dont les périodes se chevauchent");
		DemandeAbsence demandeInitiale = new DemandeAbsence();
		demandeInitiale.setDateDebut(LocalDate.now().plusDays(1));
		demandeInitiale.setDateFin(LocalDate.now().plusDays(5));
		demandeInitiale.setType(Type.CONGES_PAYES);
		
		DemandeAbsenceDTO demande = new DemandeAbsenceDTO();
		demande.setEmail("admin@dev.fr");
		demande.setDateDebut(LocalDate.now().plusDays(2));
		demande.setDateFin(LocalDate.now().plusDays(10));
		demande.setType(Type.CONGES_PAYES);
		
		Collegue collegue = new Collegue();
		
		List<DemandeAbsence> list = new ArrayList<>();
		list.add(demandeInitiale);
		
		Mockito.when(crMock.findByEmail(demande.getEmail())).thenReturn(Optional.of(collegue));
		Mockito.when(drMock.findConcurrentAbsence(demande.getDateDebut(), demande.getDateFin())).thenReturn(Optional.of(list));
		
		LOG.info("Lorsqu'on tente de sauvegarder cette demande");
		LOG.info("Alors une exception est renvoyée");
		service.enregistrerDemandeAbsence(demande);
		
	}

	@Test(expected = ModificationInvalideException.class)
	public void testModifierDemandeAbsenceAvecStatusInvalide() {
		
		LOG.info("Etant donné une demande modifiée reçue en vue d'être sauvegardée");
		
		DemandeAbsenceDTO demande = new DemandeAbsenceDTO();
		demande.setEmail("admin@dev.fr");
		demande.setDateDebut(LocalDate.now().plusDays(2));
		demande.setDateFin(LocalDate.now().plusDays(10));
		demande.setType(Type.CONGES_PAYES);
		demande.setId(3l);
		demande.setStatus(Status.VALIDEE);
		
		LOG.info("Etant donné une demande ayant un status invalide");
		
		DemandeAbsence dem = new DemandeAbsence(demande);
		dem.setStatus(Status.VALIDEE);
		
		Mockito.when(drMock.findById(demande.getId())).thenReturn(Optional.of(dem));
		
		LOG.info("Lorsqu'on tente de sauvegarder cette modification");
		LOG.info("Alors une exception est renvoyée");
		service.modifierDemande(demande, demande.getId());
		
	}
	
	@Test(expected = DemandeNonTrouveException.class)
	public void testModifierDemandeAbsenceAvecIdInvalide() {
		
		LOG.info("Etant donné une demande modifiée reçue avec un id inconnu");
		
		DemandeAbsenceDTO demande = new DemandeAbsenceDTO();
		demande.setEmail("admin@dev.fr");
		demande.setDateDebut(LocalDate.now().plusDays(2));
		demande.setDateFin(LocalDate.now().plusDays(10));
		demande.setType(Type.CONGES_PAYES);
		demande.setId(3l);
		
		DemandeAbsence dem = new DemandeAbsence(demande);
		dem.setStatus(Status.REJETEE);
		
		Mockito.when(drMock.findById(3l)).thenThrow(new DemandeNonTrouveException(""));
		
		LOG.info("Lorsqu'on tente de sauvegarder cette modification");
		LOG.info("Alors une exception est renvoyée");
		service.modifierDemande(demande, demande.getId());
		
	}

	
}
