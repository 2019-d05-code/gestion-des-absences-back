package dev.services;

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
	 * Retourne la liste des demandes en attentes de validation par le manager
	 * (contiennent en plus l'identité du demandeur)
	 * 
	 * @return List<DemandeAbsenceValidationDTO>
	 */
	public List<DemandeAbsenceValidationDTO> recupDemandesEnAttenteValidation(String email) {

		return demRepo.findAllWithStatusENAttenteValidation(email)
				.orElseThrow(() -> new CollegueNonTrouveException("Aucun manager correspondant à cet email")).stream()
				.map(demande -> new DemandeAbsenceValidationDTO(demande)).collect(Collectors.toList());

	}

	/**
	 * Permet au manager de modifier le statut d'une demande d'absence en le
	 * mettant à validé
	 * 
	 * @param id
	 */
	@Transactional
	public void validerUneDemande(Long id) {
		demRepo.findById(id)
				.orElseThrow(
						() -> new DemandeNonTrouveException("L'identifiant renseigné ne correspond à aucune demande"))
				.setStatus(Status.VALIDEE);
	}

	/**
	 * Permet au manager de modifier le statut d'une demande d'absence en le
	 * mettant à rejeté
	 * 
	 * @param id
	 */
	@Transactional
	public void rejeterUneDemande(Long id) {
		DemandeAbsence demande = demRepo.findById(id).orElseThrow(
				() -> new DemandeNonTrouveException("L'identifiant renseigné ne correspond à aucune demande"));

		demande.setStatus(Status.REJETEE);

		int decompte = TraitementNuit.recupCompteWeekends(demande.getDateDebut(), demande.getDateFin());

		if (demande.getType().equals(Type.CONGES_PAYES)) {
			demande.getCollegueConcerne()
					.setSoldeCongesPayes(demande.getCollegueConcerne().getSoldeCongesPayes() + decompte);
		}

		if (demande.getType().equals(Type.RTT)) {
			demande.getCollegueConcerne().setSoldeRTT(demande.getCollegueConcerne().getSoldeRTT() + decompte);
		}

		if (demande.getType().equals(Type.CONGES_SANS_SOLDE)) {
			demande.getCollegueConcerne()
					.setSoldeCongesSansSolde(demande.getCollegueConcerne().getSoldeCongesSansSolde() + decompte);
		}
	}

	/**
	 * Récupère la liste des demandes d'absences et les retourne sous forme de
	 * DTO
	 * 
	 * @return List<DemandeAbsenceDTO>
	 */
	// public List<Absence> demandesParMoisParCollegue(int mois, int annee) {
	//
	// LocalDate moisDateDebut = LocalDate.of(annee, mois, 1);
	// LocalDate moisDateFin = LocalDate.of(annee, mois, 28);
	//
	// // détermine combien de jours dans le mois
	// int moisPairs[] = { 4, 6, 9, 11 };
	//
	// if (Year.isLeap(annee)) {
	// int moisImpairsBiss[] = { 1, 3, 5, 7, 8, 10, 12 };
	// for (int mP : moisPairs) {
	// if (mois != mP) {
	// for (int mI : moisImpairsBiss) {
	// if (mois != mI) {
	// moisDateFin = LocalDate.of(annee, mois, 29);
	// } else {
	// moisDateFin = LocalDate.of(annee, mois, 31);
	// }
	// }
	// } else {
	// moisDateFin = LocalDate.of(annee, mois, 30);
	// }
	// }
	//
	// } else {
	// int moisImpairs[] = { 1, 3, 5, 7, 8, 10, 12 };
	// for (int mP : moisPairs) {
	// if (mois != mP) {
	// for (int mI : moisImpairs) {
	// if (mois != mI) {
	// moisDateFin = LocalDate.of(annee, mois, 28);
	// } else {
	// moisDateFin = LocalDate.of(annee, mois, 31);
	// }
	// }
	// } else {
	// moisDateFin = LocalDate.of(annee, mois, 30);
	// }
	// }
	//
	// }
	//
	// // penser à aussi trier par département
	// List<DemandeAbsenceDTO> listeDemandes =
	// demRepo.findAbsencesParMois(moisDateDebut, moisDateFin)
	// .orElseThrow(() -> new DemandeNonTrouveException("Aucune demande trouvée
	// pour cette période")).stream()
	// .map(demande -> new
	// DemandeAbsenceDTO(demande)).collect(Collectors.toList());
	//
	// // Pour chaque liste de demande de collègue, il faut extraire le jour de
	// // chaque RTT, CP et CSS
	// List<Integer> joursRTT = new ArrayList<>();
	// List<Integer> joursCP = new ArrayList<>();
	// List<Integer> joursCSS = new ArrayList<>();
	// List<Integer> joursMISSIONS = new ArrayList<>();
	//
	// for (DemandeAbsenceDTO demande : listeDemandes) {
	// if (demande.getType().equals(Type.RTT)) {
	// for (LocalDate date = demande.getDateDebut();
	// date.isBefore(demande.getDateFin()); date.plusDays(1)) {
	// joursRTT.add(date.getDayOfMonth());
	// }
	//
	// } else if (demande.getType().equals(Type.CONGES_PAYES)) {
	// for (LocalDate date = demande.getDateDebut();
	// date.isBefore(demande.getDateFin()); date.plusDays(1)) {
	// joursCP.add(date.getDayOfMonth());
	// }
	//
	// } else if (demande.getType().equals(Type.CONGES_SANS_SOLDE)) {
	// for (LocalDate date = demande.getDateDebut();
	// date.isBefore(demande.getDateFin()); date.plusDays(1)) {
	// joursCSS.add(date.getDayOfMonth());
	// }
	//
	// // TODO : rechercher les missions
	// } else if (demande.getType().equals(Type.MISSION)) {
	// for (LocalDate date = demande.getDateDebut();
	// date.isBefore(demande.getDateFin()); date.plusDays(1)) {
	// joursMISSIONS.add(date.getDayOfMonth());
	// }
	//
	// }
	//
	// }
	// }

}
