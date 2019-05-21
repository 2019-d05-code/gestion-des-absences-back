package dev.services;

import java.time.LocalDate;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.controller.vm.DemandeAbsenceDTO;
import dev.domain.Collegue;
import dev.domain.DemandeAbsence;
import dev.domain.enums.Type;
import dev.exceptions.CollegueNonTrouveException;
import dev.exceptions.DemandeInvalideException;
import dev.repository.CollegueRepo;
import dev.repository.DemandeAbsenceRepo;

/**
 * Gestion du CRUD relative aux demandes d'absence
 * 
 * @author Nicolas
 *
 */
@Service
public class DemandeAbsenceService {

	@Autowired
	DemandeAbsenceRepo demandeRepo;
	
	@Autowired
	CollegueRepo collegueRepo;
	
	public void setCollegueRepo(CollegueRepo repo) {
		collegueRepo = repo;
	}
	
	public void enregistrerDemandeAbsence(@Valid DemandeAbsenceDTO demande) {
		
		if(demande.getType().equals(Type.CONGES_SANS_SOLDE) && demande.getMotif() == null) {
			throw new DemandeInvalideException("En cas de demande de congés sans solde, un motif doit obligatoirement être fourni");
		}
		
		if(demande.getDateFin().isBefore(demande.getDateDebut())) {
			throw new DemandeInvalideException("La date de fin de l'absence ne peut pas être antérieure à la date de début");
		}
		
		if(demande.getDateDebut().isBefore(LocalDate.now().plusDays(1))) {
			throw new DemandeInvalideException("Le délai entre la demande et le début de l'absence doit être d'au moins un jour");
		}
		
		Optional<DemandeAbsence> demandeConcurrente = demandeRepo.findConcurrentAbsence(demande.getDateDebut(), demande.getDateFin());
		
		if(demandeConcurrente.isPresent()) {
			throw new DemandeInvalideException("Votre demande chevauche la période d'absence d'un autre collègue");
		}
		
		DemandeAbsence nouvelleDemande = new DemandeAbsence(demande);
		
		Collegue collegue = collegueRepo.findByEmail(demande.getEmail()).orElseThrow(() -> new CollegueNonTrouveException("Aucun collègue ayant cet email n'a été trouvé"));
		
		nouvelleDemande.setCollegueConcerne(collegue);
		
		demandeRepo.save(nouvelleDemande);
		
	}
	
}
