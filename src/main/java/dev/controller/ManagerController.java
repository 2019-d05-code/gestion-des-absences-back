package dev.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
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
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import dev.controller.vm.DemandeAbsenceDTO;
import dev.controller.vm.DemandeAbsenceValidationDTO;
import dev.controller.vm.DepartementDTO;
import dev.controller.vm.MissionDTO;
import dev.controller.vm.RapportAbsences;
import dev.controller.vm.SelectionAbsence;
import dev.domain.enums.Status;
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
	
	RestTemplate rt = new RestTemplate();

	/**
	 * Renvoie la liste des demandes en attente de validation au manager
	 * 
	 * @return List<DemandeAbsenceValidationDTO>
	 */
	@GetMapping("/listeAbsencesAttenteValidation")
	@Secured("ROLE_MANAGER")
	public ResponseEntity<List<DemandeAbsenceValidationDTO>> recupDemandesEnAttenteValidation(
			@RequestParam String email) {

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

		RapportAbsences liste = service.demandesParMoisParCollegue(select.getMois(), select.getAnnee(),
				select.getDepartement());

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

		return ResponseEntity.status(HttpStatus.OK).body(dptService.recupererDepartements());
	}
	
	/**
	 * Permet de récupérer l'ensemble des missions depuis l'API gestion des missions
	 * 
	 * @param request
	 * @return
	 * @throws RestClientException
	 * @throws URISyntaxException
	 */
	@GetMapping("/departements/missions")
	@Secured("ROLE_MANAGER")
	public ResponseEntity<List<DemandeAbsenceValidationDTO>> recupToutesLesMission(HttpServletRequest request, SelectionAbsence selection) throws RestClientException, URISyntaxException {
		
		String get = "https://missions-back.cleverapps.io/mission/";

		// On récupère le cookie et one le transfère dans la requête vers l'API
		// gestion des missions
		ResponseEntity<MissionDTO[]> respHttp2 = rt.exchange(RequestEntity.get(new URI(get))
				.header("Cookie", request.getHeader("Cookie")).build(), MissionDTO[].class);

		// On transforme la liste des missions récupérées en demandes et on les
		// injecte dans la liste des demandes
		
		List<DemandeAbsenceValidationDTO> missions = new ArrayList<>();
		
		if (respHttp2.getStatusCodeValue() == 200 && respHttp2.getBody() != null) {
			Arrays.asList(respHttp2.getBody()).stream().map(mission -> new DemandeAbsenceDTO(mission))
					.filter(demande -> service.recupListEmailDep(selection.getDepartement(), demande))
					.filter(demande -> demande.getStatus().equals(Status.VALIDEE))
					.filter(demande -> demande.getDateDebut().getMonth().getValue() == selection.getMois())
					.map(demande -> service.transformerDemande(demande))
					.collect(Collectors.toList()).forEach(demande -> missions.add(demande));
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(missions);
	}

}
