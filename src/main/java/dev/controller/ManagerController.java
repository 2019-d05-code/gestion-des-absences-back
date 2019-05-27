package dev.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.controller.vm.DemandeAbsenceValidationDTO;
import dev.services.ManagerService;

/**
 * Gestion des requêtes HTTP pour les service spécifiques au manager
 * 
 * @author Nicolas
 *
 */
@RestController
@RequestMapping("/manager")
public class ManagerController {
	
	@Autowired
	ManagerService service;

	/**
	 * Renvoie la liste des demandes en attente de validation au manager
	 * 
	 * @return List<DemandeAbsenceValidationDTO>
	 */
	@GetMapping
	@Secured("ROLE_MANAGER")
	public ResponseEntity<List<DemandeAbsenceValidationDTO>> recupDemandesEnAttenteValidation(@RequestParam String email) {
		
		List<DemandeAbsenceValidationDTO> liste = service.recupDemandesEnAttenteValidation(email);
		
		return ResponseEntity.status(HttpStatus.OK).body(liste);
	}
	
}
