package dev.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	@Secured("ROLE_UTILISATEUR")
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
	@GetMapping(path= "/listeAbsencesValidees")
	@Secured("ROLE_UTILISATEUR")
	public ResponseEntity<List<DemandeAbsenceDTO>> afficherAbsencesValideesCollegue(@RequestParam("email") String email) {

		return ResponseEntity.status(HttpStatus.OK).body(service.listeDemandesValideesParEmail(email));

	}
	
	/**
	 * Enregistre une demande de RTT employeur
	 * 
	 * @param demandes
	 * @return
	 */
	@PostMapping("/employeur-rtt")
	@Secured("ROLE_MANAGER")
	public ResponseEntity<Object> enregistrerDemandeRTTEmployeur(@RequestBody DemandeAbsenceDTO[] demandes) {
		
		service.enregistrementDemandeRTTEmployeur(demandes);
		return ResponseEntity.status(HttpStatus.OK).build();
		
	}
	
	/**
	 * Retourne la liste complète des demandes d'absences effectuées par un collègue
	 * 
	 * @param email
	 * @return List<DemandeAbsenceDTO>
	 */
	@GetMapping("/listeAbsences")
	@Secured("ROLE_UTILISATEUR")
	public ResponseEntity<List<DemandeAbsenceDTO>> recupDemandesParEmploye(@RequestParam("email") String email) {
		
		return ResponseEntity.status(HttpStatus.OK).body(service.listeDemandesParEmploye(email));
		
	}
	
	/**
	 * Modifie une demande d'absence
	 * 
	 * @param id
	 * @param demande
	 */
	@PatchMapping("/modifier/{id}")
	@Secured("ROLE_UTILISATEUR")
	public ResponseEntity<Object> modifierDemande(@PathVariable Long id, @RequestBody DemandeAbsenceDTO demande) {
		System.out.println(demande);
		System.out.println(demande.getMotif());
		service.modifierDemande(demande, id);
		
		return ResponseEntity.status(HttpStatus.OK).build();
		
	}
	
	/**
	 * Supprime une demande d'absence
	 * 
	 * @param id
	 */
	@DeleteMapping("/supprimer/{id}")
	@Secured("ROLE_UTILISATEUR")
	public ResponseEntity<Object> supprimerDemande(@PathVariable Long id) {
		
		service.supprimerDemande(id);
		
		return ResponseEntity.status(HttpStatus.OK).build();
		
	}
	
}
