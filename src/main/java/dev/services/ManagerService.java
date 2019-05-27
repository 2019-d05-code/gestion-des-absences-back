package dev.services;

import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.controller.vm.DemandeAbsenceValidationDTO;
import dev.domain.DemandeAbsence;
import dev.domain.enums.Status;
import dev.domain.enums.Type;
import dev.exceptions.CollegueNonTrouveException;
import dev.exceptions.DemandeNonTrouveException;
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
		
		return demRepo.findAllWithStatusENAttenteValidation(email).orElseThrow(() -> new CollegueNonTrouveException("Aucun manager correspondant à cet email"))
			.stream()
			.map(demande -> new DemandeAbsenceValidationDTO(demande))
			.collect(Collectors.toList());

	}
	
	/**
	 * Permet au manager de modifier le statut d'une demande d'absence en le mettant à validé
	 * 
	 * @param id
	 */
	@Transactional
	public void validerUneDemande(Long id) {
		demRepo.findById(id).orElseThrow(() -> new DemandeNonTrouveException("L'identifiant renseigné ne correspond à aucune demande"))
			.setStatus(Status.VALIDEE);
	}
	
	/**
	 * Permet au manager de modifier le statut d'une demande d'absence en le mettant à rejeté
	 * 
	 * @param id
	 */
	@Transactional
	public void rejeterUneDemande(Long id) {
		DemandeAbsence demande = demRepo.findById(id).orElseThrow(() -> new DemandeNonTrouveException("L'identifiant renseigné ne correspond à aucune demande"));
			
		demande.setStatus(Status.REJETEE);
		
		if(demande.getType().equals(Type.CONGES_PAYES)) {
			demande.getCollegueConcerne().setSoldeCongesPayes(
				demande.getCollegueConcerne().getSoldeCongesPayes() + Period.between(demande.getDateDebut(), demande.getDateFin()).getDays()
			);
		}
		
		if(demande.getType().equals(Type.RTT)) {
			demande.getCollegueConcerne().setSoldeRTT(
				demande.getCollegueConcerne().getSoldeRTT() + Period.between(demande.getDateDebut(), demande.getDateFin()).getDays()
			);
		}
		
		if(demande.getType().equals(Type.CONGES_SANS_SOLDE)) {
			demande.getCollegueConcerne().setSoldeCongesSansSolde(
				demande.getCollegueConcerne().getSoldeCongesSansSolde() + Period.between(demande.getDateDebut(), demande.getDateFin()).getDays()
			);
		}
	}
	
}
