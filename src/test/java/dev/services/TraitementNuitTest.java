package dev.services;

import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dev.domain.Collegue;
import dev.domain.DemandeAbsence;
import dev.domain.enums.Type;
import dev.repository.CollegueRepo;
import dev.repository.DemandeAbsenceRepo;

public class TraitementNuitTest {

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
	
	@Test
	public void testRecupCompteWeekendsWithValidParameters() {
		
		LOG.info("Etant donné une période de congés valide");
		LocalDate debut = LocalDate.now();
		LocalDate fin = LocalDate.now().plusDays(7);
		
		LOG.info("Quand la méthode de calcul du solde à décompter du solde de l'employé est appelé");
		int decompte = TraitementNuit.recupCompteWeekends(debut, fin);
		
		LOG.info("Alors le décompte attendu est de 5");
		Assert.assertTrue(decompte == 5);
		
	}
	
	@Test
	public void testCalculWithValidParameters() {
		
		LOG.info("Etant donné une période de congés valide");
		DemandeAbsence demande = new DemandeAbsence();
		demande.setDateDebut(LocalDate.now());
		demande.setDateFin(LocalDate.now().plusDays(7));
		demande.setType(Type.RTT);
		demande.setCollegueConcerne(new Collegue());
		
		LOG.info("Quand la méthode de calcul du solde à décompter du solde de l'employé est appelé pour décompter la période de RTT (solde initail de 10 jours)");
		int decompte = TraitementNuit.recupCompteWeekends(demande.getDateDebut(), demande.getDateFin());

		int solde = demande.getCollegueConcerne().getSoldeRTT();
		
		LOG.info("Alors le solde restant est de 5");
		Assert.assertTrue((solde - decompte) == 5);
		
	}
	
}
