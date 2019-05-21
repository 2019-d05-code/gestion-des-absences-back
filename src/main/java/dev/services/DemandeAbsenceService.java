package dev.services;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.controller.vm.DemandeAbsenceDTO;
import dev.domain.DemandeAbsence;
import dev.domain.enums.Type;
import dev.exceptions.DemandeInvalideException;
import dev.repository.DemandeAbsenceRepo;

@Service
public class DemandeAbsenceService {

	@Autowired
	DemandeAbsenceRepo demandeRepo;
	
	public void enregistrerDemandeAbsence(@Valid DemandeAbsenceDTO demande) {
		
		if(demande.getType().equals(Type.CONGES_SANS_SOLDE) && demande.getMotif() == null) {
			throw new DemandeInvalideException("En cas de demande de congès sans solde, un motif doit obligatoirement être fourni");
		}
		
		demandeRepo.save(new DemandeAbsence(demande));
		
	}
	
}
