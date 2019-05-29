package dev.services;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dev.controller.vm.DemandeAbsenceDTO;
import dev.domain.enums.Type;
import dev.exceptions.DemandeNonTrouveException;
import dev.exceptions.DepartementInvalideException;
import dev.repository.CollegueRepo;
import dev.repository.DemandeAbsenceRepo;
import dev.repository.DepartementRepo;

public class ManagerServiceTest {
	private static final Logger LOG = LoggerFactory.getLogger(DemandeAbsenceService.class);

	ManagerService service;

	private CollegueRepo crMock;

	private DemandeAbsenceRepo drMock;

	private DepartementRepo depMock;

	@Before
	public void init() {
		this.service = new ManagerService();
		this.crMock = Mockito.mock(CollegueRepo.class);
		this.service.setCollegueRepo(this.crMock);
		this.drMock = Mockito.mock(DemandeAbsenceRepo.class);
		this.service.setDemandeRepo(this.drMock);
		this.depMock = Mockito.mock(DepartementRepo.class);
		this.service.setDepartementRepo(depMock);
	}

	@Test(expected = DemandeNonTrouveException.class)
	public void testValiderUneDemande() {
		LOG.info("Etant donné une demande avec un id inexistant");
		DemandeAbsenceDTO demande = new DemandeAbsenceDTO();
		demande.setId(325l);
		demande.setEmail("admin@dev.fr");
		demande.setDateDebut(LocalDate.now().plusDays(6));
		demande.setDateFin(LocalDate.now().plusDays(5));
		demande.setType(Type.CONGES_PAYES);

		LOG.info("Lorsqu'on tente de rechercher cette demande");
		LOG.info("Alors une exception est renvoyée");

		service.validerUneDemande(demande.getId());

	}

	// Test des récupération de demandes par mois

	/**
	 * test avec déoartement null
	 */

	@Test(expected = DepartementInvalideException.class)
	public void testDemandeparMoisDptNul() {
		Long departement = null;

		service.demandesParMoisParCollegue(6, 2019, departement);

	}

	
}
