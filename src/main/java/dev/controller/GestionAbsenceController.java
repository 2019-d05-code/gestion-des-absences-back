package dev.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.controller.vm.DemandeAbsenceDTO;
import dev.services.DemandeAbsenceService;

@RestController
@RequestMapping("/gestion-absences")
public class GestionAbsenceController {

	@Autowired
	DemandeAbsenceService service;
	
	@PostMapping
	public ResponseEntity<Object> enregistrerDemandeAbsence(@RequestBody DemandeAbsenceDTO demande) {
		
		service.enregistrerDemandeAbsence(demande);
		
		return ResponseEntity.status(HttpStatus.OK).build();
		
	}
	
}
