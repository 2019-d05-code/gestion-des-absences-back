package dev.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.controller.vm.DemandeAbsenceValidationDTO;
import dev.controller.vm.DepartementDTO;
import dev.controller.vm.RapportAbsences;
import dev.controller.vm.SelectionAbsence;
import dev.services.DepartementService;
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
	
	@Autowired
	DepartementService dptService;

	/**
	 * Renvoie la liste des demandes en attente de validation au manager
	 * 
	 * @return List<DemandeAbsenceValidationDTO>
	 */
	@GetMapping("/listeAbsencesAttenteValidation")
	@Secured("ROLE_MANAGER")
	public ResponseEntity<List<DemandeAbsenceValidationDTO>> recupDemandesEnAttenteValidation(@RequestParam String email) {
		
		List<DemandeAbsenceValidationDTO> liste = service.recupDemandesEnAttenteValidation(email);
		
		return ResponseEntity.status(HttpStatus.OK).body(liste);
	}
	
	/**
	 * Permet au manager de valider une demande d'absence
	 * 
	 * @param id
	 */
	@PatchMapping("/{id}/valider")
	@Secured("ROLE_MANAGER")
	public ResponseEntity<Object> validerUneDemande(@PathVariable Long id) {
		
		service.validerUneDemande(id);
		
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	/**
	 * Permet au manager de rejeter une demande d'absence
	 * 
	 * @param id
	 */
	@PatchMapping("/{id}/rejeter")
	@Secured("ROLE_MANAGER")
	public ResponseEntity<Object> rejeterUneDemande(@PathVariable Long id) {
		
		service.rejeterUneDemande(id);
		
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	/**
	 * Renvoie la liste des demandes en attente de validation au manager
	 * 
	 * @return List<DemandeAbsenceValidationDTO>
	 */
	@PostMapping("/absencesMoisDpt")
	@Secured("ROLE_MANAGER")
	public ResponseEntity<RapportAbsences> demandesParMoisParCollegue(@RequestBody SelectionAbsence select) {
		
		RapportAbsences liste = service.demandesParMoisParCollegue(select.getMois(), select.getAnnee(), select.getDepartement());
		
		return ResponseEntity.status(HttpStatus.OK).body(liste);
	}
	
	/**
	 * Renvoie la liste des départements
	 * 
	 * @return List<DepartementDTO>
	 */
	@GetMapping("/departements")
	@Secured("ROLE_MANAGER")
	public ResponseEntity<List<DepartementDTO>> recupDepartements() {
		
		List<DepartementDTO> liste = dptService.recupererDepartements();
		
		return ResponseEntity.status(HttpStatus.OK).body(liste);
	}
	
}
