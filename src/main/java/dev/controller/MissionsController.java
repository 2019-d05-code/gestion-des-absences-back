package dev.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import dev.controller.vm.MissionDTO;
import dev.domain.Collegue;
import dev.repository.CollegueRepo;

@RestController
public class MissionsController {

	@Autowired
	CollegueRepo colRepo;
	
	String URL_CONNEXION_MISSIONS = "${ auth }";
	String URL_MISSIONS = "${ get }";
	
	RestTemplate rt = new RestTemplate();
	
	/**
	 * Permet de récupérer les missions
	 * 
	 * @param email
	 * @param request
	 * @return
	 * @throws RestClientException
	 * @throws URISyntaxException
	 */
	@GetMapping("/missions")
	public ResponseEntity<List<MissionDTO>> recupMissionsDuCollegue(@RequestParam String email, HttpServletRequest request) throws RestClientException, URISyntaxException {
		
		String get = "https://missions-back.cleverapps.io/collegue/";
		
		// On récupère le collègue connecté afin d'avoir ses identifiants de connexion
		Collegue collegueConnecte = colRepo.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("L'email ne correspond à aucun collegue"));
		
		// On récupère le cookie et one le transfère dans la requête vers l'API gestion des missions
		ResponseEntity<MissionDTO[]> respHttp2 = rt.exchange(RequestEntity
				.get(new URI(get + collegueConnecte.getId()))
				.header("Cookie", request.getHeader("Cookie"))
				.build(), MissionDTO[].class);
		
		MissionDTO[] missions =  respHttp2.getBody();
		
		List<MissionDTO> list = Arrays.asList(missions);
		
		return ResponseEntity.status(HttpStatus.OK).body(list);
		
	}
	
}
