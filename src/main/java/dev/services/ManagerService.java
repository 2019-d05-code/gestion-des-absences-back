package dev.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.controller.vm.DemandeAbsenceValidationDTO;
import dev.repository.CollegueRepo;
import dev.repository.DemandeAbsenceRepo;

@Service
public class ManagerService {

	@Autowired
	CollegueRepo colRepo;
	
	@Autowired
	DemandeAbsenceRepo demRepo;
	
	/**
	 * Retourne la liste des demandes en attentes de validation par le manager (contiennent en plus l'identité du demandeur)
	 * 
	 * @return List<DemandeAbsenceValidationDTO>
	 */
	public List<DemandeAbsenceValidationDTO> recupDemandesEnAttenteValidation(String email) {
		
		return demRepo.findAllWithStatusENAttenteValidation(email).orElseThrow(() -> new IllegalArgumentException("Aucun manager correspondant à cet email"))
			.stream()
			.map(demande -> new DemandeAbsenceValidationDTO(demande))
			.collect(Collectors.toList());

	}
	
}
