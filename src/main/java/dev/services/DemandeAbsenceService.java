package dev.services;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.controller.vm.DemandeAbsenceDTO;
import dev.controller.vm.JourFerieDTO;
import dev.domain.Collegue;
import dev.domain.DemandeAbsence;
import dev.domain.JourFerie;
import dev.domain.enums.Status;
import dev.domain.enums.Type;
import dev.exceptions.CollegueNonTrouveException;
import dev.exceptions.DemandeInvalideException;
import dev.exceptions.DemandeNonTrouveException;
import dev.exceptions.ModificationInvalideException;
import dev.repository.CollegueRepo;
import dev.repository.DemandeAbsenceRepo;
import dev.repository.JourFerieRepository;

/**
 * Gestion du CRUD relative aux demandes d'absence
 * 
 * @author Nicolas
 *
 */
@Service
public class DemandeAbsenceService {

	@Autowired
	DemandeAbsenceRepo demandeRepo;

	@Autowired
	CollegueRepo collegueRepo;

	@Autowired
	JourFerieRepository jfRepo;

	/**
	 * Permet d'utiliser un Mock à la place du vrai repository pour les tests
	 * unitaires
	 * 
	 * @param repo
	 */
	public void setCollegueRepo(CollegueRepo repo) {
		collegueRepo = repo;
	}

	/**
	 * Permet d'utiliser un Mock à la place du vrai repository pour les tests
	 * unitaires
	 * 
	 * @param repo
	 */
	public void setDemandeRepo(DemandeAbsenceRepo repo) {
		demandeRepo = repo;
	}

	/**
	 * Sauvegarde une nouvelle demande en attendant quelle soit traitée
	 * (traitement de nuit)
	 * 
	 * @param demande
	 */
	public void enregistrerDemandeAbsence(@Valid DemandeAbsenceDTO demande) {

		if (demande.getType().equals(Type.CONGES_SANS_SOLDE) && demande.getMotif() == null) {
			throw new DemandeInvalideException(
					"En cas de demande de congés sans solde, un motif doit obligatoirement être fourni");
		}

		if (demande.getDateFin().isBefore(demande.getDateDebut())) {
			throw new DemandeInvalideException(
					"La date de fin de l'absence ne peut pas être antérieure à la date de début");
		}

		if (demande.getDateDebut().isBefore(LocalDate.now().plusDays(1))) {
			throw new DemandeInvalideException(
					"Le délai entre la demande et le début de l'absence doit être d'au moins un jour");
		}

		Optional<List<DemandeAbsence>> demandeConcurrentes = demandeRepo.findConcurrentAbsence(demande.getDateDebut(),
				demande.getDateFin(), demande.getEmail());

		if (demandeConcurrentes.isPresent() && !demandeConcurrentes.get().isEmpty()) {
			throw new DemandeInvalideException(
					"Votre demande chevauche la période d'absence d'une autre de vos demandes");
		}

		DemandeAbsence nouvelleDemande = new DemandeAbsence(demande);

		Collegue collegue = collegueRepo.findByEmail(demande.getEmail())
				.orElseThrow(() -> new CollegueNonTrouveException("Aucun collègue ayant cet email n'a été trouvé"));

		nouvelleDemande.setCollegueConcerne(collegue);

		demandeRepo.save(nouvelleDemande);

	}

	/**
	 * Enregistre une demande d'absence de type RTT employeur au status initial
	 * 
	 * @param demandes
	 */
	public void enregistrementDemandeRTTEmployeur(JourFerieDTO rtteDTO) {

		LocalDate date = rtteDTO.getDate();

		if (date.getDayOfWeek().equals(DayOfWeek.SATURDAY) || date.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
			throw new DemandeInvalideException("Vous ne pouvez pas déclarer un RTT employeur un jour de weekend");
		}

		if (jfRepo.findJourFerie(date) != null) {
			throw new DemandeInvalideException("Un jour férié ou un RTT employeur existe déjà à cette date");
		}

		JourFerie rtte = new JourFerie(rtteDTO);
		jfRepo.save(rtte);

		List<Collegue> employes = collegueRepo.findAll();

		employes.forEach(collegue -> {

			DemandeAbsence demandeRTTE = new DemandeAbsence();
			demandeRTTE.setCollegueConcerne(collegue);
			demandeRTTE.setDateDebut(rtteDTO.getDate());
			demandeRTTE.setDateFin(rtteDTO.getDate());
			if(rtteDTO.getCommentaire() != null) {
				demandeRTTE.setMotif(rtteDTO.getCommentaire());				
			}
			demandeRTTE.setType(Type.RTT_EMPLOYEUR);
			demandeRTTE.setStatus(Status.INITIALE);

			demandeRepo.save(demandeRTTE);
		});

	}

	/**
	 * Enregistre une demande de RTT employeur validée par le traitement de nuit
	 * 
	 * @param demande
	 */
	public void enregistrementDemandeRTTEmployeurTN(@Valid DemandeAbsence demande) {

		demandeRepo.save(demande);
		collegueRepo.save(demande.getCollegueConcerne());

	}

	/**
	 * Récupère la liste des demandes d'absences et les retourne sous forme de
	 * DTO
	 * 
	 * @return List<DemandeAbsenceDTO>
	 */
	public List<DemandeAbsenceDTO> listeDemandesParEmploye(String email) {
		return demandeRepo.findAllByEmail(email)
				.orElseThrow(() -> new CollegueNonTrouveException("Aucun collègue ayant cet email n'a été trouvé"))
				.stream().map(demande -> new DemandeAbsenceDTO(demande)).collect(Collectors.toList());
	}

	/**
	 * Récupère la liste des demandes d'absence validées et les retourne sous
	 * forme de DTO
	 * 
	 * @param email
	 * @return List<DemandeAbsenceDTO>
	 */
	public List<DemandeAbsenceDTO> listeDemandesValideesParEmail(String email) {
		return demandeRepo.findByCollegueConcerneEmail(email)
				.orElseThrow(() -> new CollegueNonTrouveException("Aucun collègue ayant cet email n'a été trouvé"))
				.stream().filter(demande -> demande.getStatus().equals(Status.VALIDEE))
				.map(demande -> new DemandeAbsenceDTO(demande)).collect(Collectors.toList());
	}

	/**
	 * Modifie la demande d'absence souhaitée à condition qu'elle n'ai pas le
	 * status REJETEE ou VALIDEE
	 * 
	 * @param demande
	 * @param id
	 */
	@Transactional
	public void modifierDemande(DemandeAbsenceDTO demande, Long id) {

		DemandeAbsence demandeAModif = demandeRepo.findById(id).orElseThrow(
				() -> new DemandeNonTrouveException("Il n'y a aucune demande d'absence ayant cet identifiant"));

		if (demandeAModif.getStatus().equals(Status.VALIDEE) && demandeAModif.getType().equals(Type.RTT_EMPLOYEUR)) {
			throw new ModificationInvalideException(
					"Vous ne pouvez pas modifier une demande validée de type RTT employeur");
		}

		if (demandeAModif.getStatus().equals(Status.EN_ATTENTE_VALIDATION)
				|| demandeAModif.getStatus().equals(Status.VALIDEE)) {
			throw new ModificationInvalideException(
					"Vous ne pouvez pas modifier une demande validée ou en attente de validation");
		}

		demandeAModif.setDateDebut(demande.getDateDebut());
		demandeAModif.setDateFin(demande.getDateFin());
		demandeAModif.setMotif(demande.getMotif());
		demandeAModif.setType(demande.getType());
		demandeAModif.setStatus(Status.INITIALE);

	}

	/**
	 * Supprime la demande d'absence de la base de données
	 * 
	 * @param id
	 */
	public void supprimerDemande(Long id) {
		demandeRepo.deleteById(id);
	}

	/**
	 * Récupère la liste des demandes avec le status INITIALE en vue du
	 * traitement de nuit
	 * 
	 * @return List<DemandeAbsence>
	 */
	public List<DemandeAbsence> listeDemandesInitialesTN() {
		return demandeRepo.findByStatus().orElse(new ArrayList<DemandeAbsence>());
	}

	/**
	 * Sauvegarde une demande modifiée par le traitement de nuit
	 * 
	 * @param demande
	 */
	public void sauvegarderModifDemandesTN(DemandeAbsence demande) {

		demandeRepo.save(demande);
		collegueRepo.save(demande.getCollegueConcerne());

	}

	/**
	 * Permet de sauvegarder une demande d'absence générée à partir d'un jour
	 * férié
	 * 
	 * @param demandes
	 */
	public void sauvegarderJourFeriee(JourFerieDTO jourFerieDTO) {

		if (jourFerieDTO.getType().equals(Type.FERIE) && jourFerieDTO.getCommentaire() == null) {
			throw new DemandeInvalideException("En cas de jour férié, un commentaire doit obligatoirement être fourni");
		}

		if (jfRepo.findJourFerie(jourFerieDTO.getDate()) != null) {
			throw new DemandeInvalideException("Un jour férié ou un RTT employeur existe déjà à la date fournie");
		}

		if (jourFerieDTO.getType().equals(Type.FERIE) && jourFerieDTO.getDate().isBefore(LocalDate.now())) {
			throw new DemandeInvalideException("Il est interdit de saisir un jour férié dans le passé");
		}

		JourFerie jourFerie = new JourFerie(jourFerieDTO);
		jfRepo.save(jourFerie);

		List<Collegue> employes = collegueRepo.findAll();

		employes.forEach(collegue -> {
			DemandeAbsence demandeJF = new DemandeAbsence();
			demandeJF.setCollegueConcerne(collegue);
			demandeJF.setDateDebut(jourFerieDTO.getDate());
			demandeJF.setDateFin(jourFerieDTO.getDate());
			demandeJF.setType(Type.FERIE);
			demandeJF.setStatus(Status.VALIDEE);

			demandeRepo.save(demandeJF);

		});

	}

	/**
	 * Récupère la liste des jours fériés et RTT-employeur
	 * 
	 * @return List<JourFerieDTO>
	 */
	public List<JourFerieDTO> recupAbsenceCollective(int annee) {

		return jfRepo.findAll().stream().filter(jf -> jf.getDate().getYear() == annee).map(jf -> new JourFerieDTO(jf)).collect(Collectors.toList());

	}

	/**
	 * Modifie une absence collective (jour férié ou RTT employeur)
	 * 
	 * @param jourFerieDTO
	 * @param id
	 */
	@Transactional
	public void modifierDemandeJFOuRTTE(JourFerieDTO jourFerieDTO, Long id) {

		JourFerie jourFerieAModif = jfRepo.findById(id).orElseThrow(() -> new IllegalArgumentException(
				"Cet identifiant ne correspond à aucun jour férié ou RTT employeur"));

		if (jourFerieDTO.getType().equals(Type.FERIE) && jourFerieDTO.getCommentaire() == null) {
			throw new DemandeInvalideException("En cas de jour férié, un commentaire doit obligatoirement être fourni");
		}
		
		if (jourFerieDTO.getType().equals(Type.RTT_EMPLOYEUR) && 
				(jourFerieDTO.getDate().getDayOfWeek().equals(DayOfWeek.SATURDAY) || 
						jourFerieDTO.getDate().getDayOfWeek().equals(DayOfWeek.SUNDAY))) {
			throw new DemandeInvalideException("Vous ne pouvez pas déclarer un RTT employeur un jour de weekend");
		}

		if (jfRepo.findJourFerie(jourFerieDTO.getDate()) != null) {
			throw new DemandeInvalideException("Un jour férié ou un RTT employeur existe déjà à la date fournie");
		}

		if (jourFerieDTO.getType().equals(Type.FERIE) && jourFerieDTO.getDate().isBefore(LocalDate.now())) {
			throw new DemandeInvalideException("Il est interdit de saisir un jour férié dans le passé");
		}
		
		List<DemandeAbsence> demandes = demandeRepo.findAll().stream()
				.filter(demande -> demande.getType().equals(jourFerieAModif.getType())
						&& demande.getDateDebut().equals(jourFerieAModif.getDate()))
				.filter(demande -> !demande.getStatus().equals(Status.VALIDEE))
				.collect(Collectors.toList());
		
		if(demandes.get(0) != null && demandes.get(0).getType().equals(Type.RTT_EMPLOYEUR) && demandes.get(0).getStatus().equals(Status.VALIDEE)) {
			throw new DemandeInvalideException("Il est interdit de modifier un RTT employeur validé");
		}

		demandes.forEach(demande -> {
			demande.setDateDebut(jourFerieDTO.getDate());
			demande.setDateFin(jourFerieDTO.getDate());
			demande.setMotif(jourFerieDTO.getCommentaire());				
		});
		
		jourFerieAModif.setDate(jourFerieDTO.getDate());
		jourFerieAModif.setCommentaire(jourFerieDTO.getCommentaire());
	}

	/**
	 * Supprime une absence collective (jour férié ou RTT employeur)
	 * 
	 * @param id
	 */
	public void supprimerAbsenceCollective(Long id) {
		
		JourFerie jourFerieASupprimer = jfRepo.findById(id).orElseThrow(() -> new IllegalArgumentException(
				"Cet identifiant ne correspond à aucun jour férié ou RTT employeur"));

		if (jourFerieASupprimer.getDate().isBefore(LocalDate.now())) {
			throw new DemandeInvalideException("Il est interdit de supprimer un jour férié ou un RTT employeur dans le passé");
		}
		
		List<DemandeAbsence> demandes = demandeRepo.findAll().stream()
				.filter(demande -> demande.getType().equals(jourFerieASupprimer.getType())
						&& demande.getDateDebut().equals(jourFerieASupprimer.getDate()))
				.collect(Collectors.toList());
		
		if(demandes.get(0) != null && demandes.get(0).getType().equals(Type.RTT_EMPLOYEUR) && demandes.get(0).getStatus().equals(Status.VALIDEE)) {
			throw new DemandeInvalideException("Il est interdit de supprimer un RTT employeur validé");
		}

		demandes.forEach(demande -> demandeRepo.delete(demande));
		
		jfRepo.deleteById(id);
		
	}

}
