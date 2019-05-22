package dev.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import dev.controller.vm.DemandeAbsenceDTO;
import dev.services.DemandeAbsenceService;

@RestController
@RequestMapping("/gestion-absences")
public class GestionAbsenceController {

	@Autowired
	DemandeAbsenceService service;
	
	/**
	 * Permet d'appeler le service checkant la validité de la demande et le cas échéant l'enregistrant
	 * 
	 * @param demande
	 * @return
	 */
	@PostMapping
	public ResponseEntity<Object> enregistrerDemandeAbsence(@RequestBody DemandeAbsenceDTO demande) {
		
		service.enregistrerDemandeAbsence(demande);
		
		return ResponseEntity.status(HttpStatus.OK).build();
		
	}
	
	/**
	 * Récupère la liste des demandes d'absences validées d'un collègue
	 * 
	 * @param email
	 * @return
	 */
	@GetMapping(path= "/listeAbsence")
	@ResponseBody
	public List<DemandeAbsenceDTO> afficherAbsencesCollegue(@RequestParam("email") String email) {

		return service.listeDemandesValideesParEmail(email);

	}
	
	
}
