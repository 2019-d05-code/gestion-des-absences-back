package dev.services;

import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.controller.vm.DemandeAbsenceDTO;
import dev.domain.Collegue;
import dev.domain.enums.Type;
import dev.exceptions.CollegueNonTrouveException;
import dev.repository.CollegueRepo;
import dev.repository.DemandeAbsenceRepo;

/**
 * Gestion du CRUD relative à l'affichage des absences coté manager
 * 
 * @author Julie
 *
 */
@Service
public class ManagerService {

	@Autowired
	DemandeAbsenceRepo demandeRepo;

	@Autowired
	CollegueRepo collegueRepo;

	/**
	 * Récupère la liste des demandes d'absences et les retourne sous forme de
	 * DTO
	 * 
	 * @return List<DemandeAbsenceDTO>
	 */
	public List<Absences> demandesParMoisParCollegue(int mois, int annee) {
		// vérification que le collegue existe
		Collegue collegue = collegueRepo.findByEmail(email)
				.orElseThrow(() -> new CollegueNonTrouveException("Aucun collègue ayant cet email n'a été trouvé"));

		LocalDate moisDateDebut = LocalDate.of(annee, mois, 1);
		LocalDate moisDateFin = LocalDate.of(annee, mois, 28);

		// détermine combien de jours dans le mois
		int moisPairs[] = { 4, 6, 9, 11 };

		if (Year.isLeap(annee)) {
			int moisImpairsBiss[] = { 1, 3, 5, 7, 8, 10, 12 };
			for (int mP : moisPairs) {
				if (mois != mP) {
					for (int mI : moisImpairsBiss) {
						if (mois != mI) {
							moisDateFin = LocalDate.of(annee, mois, 29);
						} else {
							moisDateFin = LocalDate.of(annee, mois, 31);
						}
					}
				} else {
					moisDateFin = LocalDate.of(annee, mois, 30);
				}
			}

		} else {
			int moisImpairs[] = { 1, 3, 5, 7, 8, 10, 12 };
			for (int mP : moisPairs) {
				if (mois != mP) {
					for (int mI : moisImpairs) {
						if (mois != mI) {
							moisDateFin = LocalDate.of(annee, mois, 28);
						} else {
							moisDateFin = LocalDate.of(annee, mois, 31);
						}
					}
				} else {
					moisDateFin = LocalDate.of(annee, mois, 30);
				}
			}

		}
		
		// trouver tous les collègues et tous leurs emails
		
		// boucler sur chaque collegue
		String email = null;

		// penser à aussi trier par département
		List<DemandeAbsenceDTO> listeDemandes = demandeRepo.findAbsencesParMois(email, moisDateDebut, moisDateFin);
		// Pour chaque liste de demande de collègue, il faut extraire le jour de
		// chaque RTT, CP et CSS
		List<Integer> joursRTT = new ArrayList<>();
		List<Integer> joursCP = new ArrayList<>();
		List<Integer> joursCSS = new ArrayList<>();
		List<Integer> joursMISSIONS = new ArrayList<>();

		for (DemandeAbsenceDTO demande : listeDemandes) {
			if (demande.getType().equals(Type.RTT)) {
				for (LocalDate date = demande.getDateDebut(); date.isBefore(demande.getDateFin()); date.plusDays(1)) {
					joursRTT.add(date.getDayOfMonth());
				}

			} else if (demande.getType().equals(Type.CONGES_PAYES)) {
				for (LocalDate date = demande.getDateDebut(); date.isBefore(demande.getDateFin()); date.plusDays(1)) {
					joursCP.add(date.getDayOfMonth());
				}

			} else if (demande.getType().equals(Type.CONGES_SANS_SOLDE)) {
				for (LocalDate date = demande.getDateDebut(); date.isBefore(demande.getDateFin()); date.plusDays(1)) {
					joursCSS.add(date.getDayOfMonth());
				}

				//TODO : rechercher les missions
			} else if (demande.getType().equals(Type.MISSION)) {
				for (LocalDate date = demande.getDateDebut(); date.isBefore(demande.getDateFin()); date.plusDays(1)) {
					joursMISSIONS.add(date.getDayOfMonth());
				}

			}

		}
	}

}
