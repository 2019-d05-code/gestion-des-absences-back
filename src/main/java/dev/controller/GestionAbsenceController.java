package dev.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
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
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import dev.controller.vm.DemandeAbsenceDTO;
import dev.controller.vm.MissionDTO;
import dev.domain.Collegue;
import dev.repository.CollegueRepo;
import dev.services.DemandeAbsenceService;

@RestController
@RequestMapping("/gestion-absences")
public class GestionAbsenceController {

	@Autowired
	DemandeAbsenceService service;

	@Autowired
	CollegueRepo colRepo;

	RestTemplate rt = new RestTemplate();

	/**
	 * Permet d'appeler le service checkant la validité de la demande et le cas
	 * échéant l'enregistrant
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
	@GetMapping(path = "/listeAbsencesValidees")
	@Secured("ROLE_UTILISATEUR")
	public ResponseEntity<List<DemandeAbsenceDTO>> afficherAbsencesValideesCollegue(
			@RequestParam("email") String email) {

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
	 * Retourne la liste complète des demandes d'absences effectuées par un
	 * collègue ainsi que ses missions (considérées comme des absences)
	 * 
	 * @param email
	 * @return List<DemandeAbsenceDTO>
	 * @throws URISyntaxException
	 * @throws RestClientException
	 */
	@GetMapping("/listeAbsences")
	@Secured("ROLE_UTILISATEUR")
	public ResponseEntity<List<DemandeAbsenceDTO>> recupDemandesParEmploye(@RequestParam("email") String email,
			HttpServletRequest request) throws RestClientException, URISyntaxException {

		String get = "https://missions-back.cleverapps.io/collegue/id/";

		// On récupère le collègue connecté afin d'avoir ses identifiants de
		// connexion
		Collegue collegueConnecte = colRepo.findByEmail(email)
				.orElseThrow(() -> new IllegalArgumentException("L'email ne correspond à aucun collegue"));

		// On récupère la liste des demandes
		List<DemandeAbsenceDTO> listeDemandes = service.listeDemandesParEmploye(email);

		// On récupère le cookie et one le transfère dans la requête vers l'API
		// gestion des missions
		ResponseEntity<MissionDTO[]> respHttp2 = rt.exchange(RequestEntity.get(new URI(get + collegueConnecte.getId()))
				.header("Cookie", request.getHeader("Cookie")).build(), MissionDTO[].class);

		// On transforme la liste des missions récupérées en demandes et on les
		// injecte dans la liste des demandes
		if (respHttp2.getStatusCodeValue() == 200 && respHttp2.getBody() != null) {
			Arrays.asList(respHttp2.getBody()).stream().map(mission -> new DemandeAbsenceDTO(mission, email))
					.collect(Collectors.toList()).forEach(demande -> listeDemandes.add(demande));
		}

		return ResponseEntity.status(HttpStatus.OK).body(listeDemandes);

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
