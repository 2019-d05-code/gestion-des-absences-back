package dev.services;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.controller.vm.Absences;
import dev.controller.vm.DemandeAbsenceDTO;
/**
 * Gestion du CRUD relative à l'affichage des absences coté manager
 * 
 * @author Julie
 *
 */
import dev.controller.vm.DemandeAbsenceValidationDTO;
import dev.controller.vm.RapportAbsences;
import dev.domain.Collegue;
import dev.domain.DemandeAbsence;
import dev.domain.enums.Status;
import dev.domain.enums.Type;
import dev.exceptions.CollegueNonTrouveException;
import dev.exceptions.DemandeNonTrouveException;
import dev.exceptions.DepartementInvalideException;
import dev.repository.CollegueRepo;
import dev.repository.DemandeAbsenceRepo;
import dev.repository.DepartementRepo;

@Service
public class ManagerService {

	@Autowired
	CollegueRepo colRepo;

	@Autowired
	DemandeAbsenceRepo demRepo;

	@Autowired
	DepartementRepo depRepo;

	/**
	 * Permet d'utiliser un Mock à la place du vrai repository pour les tests
	 * unitaires
	 * 
	 * @param repo
	 */
	public void setCollegueRepo(CollegueRepo repo) {
		colRepo = repo;
	}

	/**
	 * Permet d'utiliser un Mock à la place du vrai repository pour les tests
	 * unitaires
	 * 
	 * @param repo
	 */
	public void setDemandeRepo(DemandeAbsenceRepo repo) {
		demRepo = repo;
	}
	
	/**
	 * Permet d'utiliser un Mock à la place du vrai repository pour les tests
	 * unitaires
	 * 
	 * @param repo
	 */
	public void setDepartementRepo(DepartementRepo repo) {
		depRepo = repo;
	}


	/**
	 * Retourne la liste des demandes en attentes de validation par le manager
	 * (contiennent en plus l'identité du demandeur)
	 * 
	 * @return List<DemandeAbsenceValidationDTO>
	 */
	public List<DemandeAbsenceValidationDTO> recupDemandesEnAttenteValidation(String email) {

		return demRepo.findAllWithStatusENAttenteValidation(email).stream()
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
	 * Récupère la liste des demandes d'absences par collègue et par mois et les
	 * retourne sous forme de d'une rapport avec les week-end du mois et les
	 * demandes d'absences associées
	 * 
	 * @return RapportAbsences
	 */
	public RapportAbsences demandesParMoisParCollegue(Integer mois, Integer annee, Long departement) {

		if (departement == null) {
			throw new DepartementInvalideException(
					"Vous n'avez renseigné aucun département");
		}

		// penser à vérifer département non existant
		depRepo.findById(departement).orElseThrow(() -> new DepartementInvalideException(
				"Le département que vous avez choisi ne figure pas dans la base de données"));


		LocalDate moisDateDebut = LocalDate.of(annee, mois, 1);
		LocalDate moisDateFinLocal = LocalDate.of(annee, mois, 28);
		int nbJourMois = moisDateFinLocal.lengthOfMonth();

		LocalDate moisDateFin = LocalDate.of(annee, mois, nbJourMois);

		List<DemandeAbsence> demandes = demRepo.findAbsencesParMoisParDepartement(moisDateDebut, moisDateFin,
				departement);
		List<Absences> listeAbsences = new ArrayList<Absences>();

		for (DemandeAbsence uneDemande : demandes) {

			// Pour chaque liste de demande de collègue, il faut extraire le
			// jour de
			// chaque RTT, CP et CSS
			List<Integer> joursRTT = new ArrayList<>();
			List<Integer> joursCP = new ArrayList<>();
			List<Integer> joursCSS = new ArrayList<>();
			List<Integer> joursMISSIONS = new ArrayList<>();

			if (uneDemande.getType().equals(Type.RTT)) {
				if (uneDemande.getDateFin().getMonthValue() == mois
						&& uneDemande.getDateDebut().getMonthValue() == mois) {
					for (LocalDate date = uneDemande.getDateDebut(); date
							.isBefore(uneDemande.getDateFin().plusDays(1)); date = date.plusDays(1)) {
						joursRTT.add(date.getDayOfMonth());

					}
				} else if (uneDemande.getDateDebut().getMonthValue() == mois) {
					for (LocalDate date = uneDemande.getDateDebut(); date
							.isBefore(moisDateFin.plusDays(1)); date = date.plusDays(1)) {
						joursRTT.add(date.getDayOfMonth());
					}
				} else if (uneDemande.getDateFin().getMonthValue() == mois) {
					for (LocalDate date = moisDateDebut; date
							.isBefore(uneDemande.getDateFin().plusDays(1)); date = date.plusDays(1)) {
						joursRTT.add(date.getDayOfMonth());
					}
				}

			} else if (uneDemande.getType().equals(Type.CONGES_PAYES)) {
				if (uneDemande.getDateFin().getMonthValue() == mois
						&& uneDemande.getDateDebut().getMonthValue() == mois) {
					for (LocalDate date = uneDemande.getDateDebut(); date
							.isBefore(uneDemande.getDateFin().plusDays(1)); date = date.plusDays(1)) {
						joursCP.add(date.getDayOfMonth());

					}
				} else if (uneDemande.getDateDebut().getMonthValue() == mois) {
					for (LocalDate date = uneDemande.getDateDebut(); date
							.isBefore(moisDateFin.plusDays(1)); date = date.plusDays(1)) {
						joursCP.add(date.getDayOfMonth());
					}
				} else if (uneDemande.getDateFin().getMonthValue() == mois) {
					for (LocalDate date = moisDateDebut; date
							.isBefore(uneDemande.getDateFin().plusDays(1)); date = date.plusDays(1)) {
						joursCP.add(date.getDayOfMonth());
					}
				}

			} else if (uneDemande.getType().equals(Type.CONGES_SANS_SOLDE)) {
				if (uneDemande.getDateFin().getMonthValue() == mois
						&& uneDemande.getDateDebut().getMonthValue() == mois) {
					for (LocalDate date = uneDemande.getDateDebut(); date
							.isBefore(uneDemande.getDateFin().plusDays(1)); date = date.plusDays(1)) {
						joursCSS.add(date.getDayOfMonth());

					}
				} else if (uneDemande.getDateDebut().getMonthValue() == mois) {
					for (LocalDate date = uneDemande.getDateDebut(); date
							.isBefore(moisDateFin.plusDays(1)); date = date.plusDays(1)) {
						joursCSS.add(date.getDayOfMonth());
					}
				} else if (uneDemande.getDateFin().getMonthValue() == mois) {
					for (LocalDate date = moisDateDebut; date
							.isBefore(uneDemande.getDateFin().plusDays(1)); date = date.plusDays(1)) {
						joursCSS.add(date.getDayOfMonth());
					}
				}

			}

			String prenomCollegue = uneDemande.getCollegueConcerne().getPrenom();
			String nomCollegue = uneDemande.getCollegueConcerne().getNom();
			listeAbsences.add(new Absences(nomCollegue, prenomCollegue, joursRTT, joursCP, joursCSS));

		}

		// Implémentation du tableau des jours correspondants au week-end
		List<Integer> weekEnd = new ArrayList<>();
		for (LocalDate date = moisDateDebut; date.isBefore(moisDateFin); date = date.plusDays(1)) {
			if (date.getDayOfWeek().equals(DayOfWeek.SATURDAY) || date.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
				weekEnd.add(date.getDayOfMonth());

			}
		}

		// Ajout du tableau d'absences dans le rapport

		return new RapportAbsences(weekEnd, listeAbsences);
	}
	
	public boolean recupListEmailDep(long id, DemandeAbsenceDTO demande) {
		
		boolean exist = false;
		
		Collegue collegue = colRepo.findByEmail(demande.getEmail()).orElseThrow(() -> new CollegueNonTrouveException("Aucun collègue ayant cet email n'a été trouvé"));
		
		if(collegue.getDepartement().getId() == id) {
			exist = true;
		}
		
		return exist;
	}
	
	public DemandeAbsenceValidationDTO transformerDemande(DemandeAbsenceDTO demande) {
		Collegue collegue = colRepo.findByEmail(demande.getEmail()).orElseThrow(() -> new CollegueNonTrouveException("Aucun collègue ayant cet email n'a été trouvé"));
		return new DemandeAbsenceValidationDTO(demande, collegue.getNom(), collegue.getPrenom());
	}

}
