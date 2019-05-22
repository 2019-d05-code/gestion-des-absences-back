package dev.services;

import java.time.Period;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import dev.domain.DemandeAbsence;
import dev.domain.enums.Status;
import dev.repository.CollegueRepo;

@Configuration
@EnableScheduling
public class TraitementNuit {

	@Autowired
	DemandeAbsenceService serviceDemande;

	@Autowired
	CollegueRepo collegueRepo;

	@Scheduled(cron="0 0 * * *")
	@Transactional
	public void traitementNocturne() {

		List<DemandeAbsence> demandesATraiter = this.serviceDemande.listeDemandesInitialesTN();

		for (DemandeAbsence demande : demandesATraiter) {

			// Pour les congés payés
			if (demande.getType().equals("CONGES_PAYES") && (demande.getCollegueConcerne()
					.getSoldeCongesPayes() >= Period.between(demande.getDateDebut(), demande.getDateFin()).getDays())) {
				demande.setStatus(Status.EN_ATTENTE_VALIDATION);
				demande.getCollegueConcerne().setSoldeCongesPayes(demande.getCollegueConcerne().getSoldeCongesPayes()
						- Period.between(demande.getDateDebut(), demande.getDateFin()).getDays());
			} else if (demande.getType().equals("CONGES_PAYES") && (demande.getCollegueConcerne()
					.getSoldeCongesPayes() < Period.between(demande.getDateDebut(), demande.getDateFin()).getDays())) {
				demande.setStatus(Status.REJETEE);
			}

			// Pour les RTT
			if (demande.getType().equals("RTT") && (demande.getCollegueConcerne().getSoldeRTT() >= Period
					.between(demande.getDateDebut(), demande.getDateFin()).getDays())) {
				demande.setStatus(Status.EN_ATTENTE_VALIDATION);
				demande.getCollegueConcerne().setSoldeRTT(demande.getCollegueConcerne().getSoldeRTT()
						- Period.between(demande.getDateDebut(), demande.getDateFin()).getDays());
			} else if (demande.getType().equals("RTT") && (demande.getCollegueConcerne().getSoldeRTT() < Period
					.between(demande.getDateDebut(), demande.getDateFin()).getDays())) {
				demande.setStatus(Status.REJETEE);
			}

			// Pour les congés sans solde
			if (demande.getType().equals("CONGES_SANS_SOLDE") && (demande.getCollegueConcerne().getSoldeCongesSansSolde() >= Period
					.between(demande.getDateDebut(), demande.getDateFin()).getDays())) {
				demande.setStatus(Status.EN_ATTENTE_VALIDATION);
				demande.getCollegueConcerne().setSoldeCongesSansSolde(demande.getCollegueConcerne().getSoldeCongesSansSolde()
						- Period.between(demande.getDateDebut(), demande.getDateFin()).getDays());
			} else if (demande.getType().equals("CONGES_SANS_SOLDE") && (demande.getCollegueConcerne().getSoldeCongesSansSolde() < Period
					.between(demande.getDateDebut(), demande.getDateFin()).getDays())) {
				demande.setStatus(Status.REJETEE);
			}

		}

	}

}
