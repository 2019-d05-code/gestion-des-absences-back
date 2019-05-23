package dev.services;

import java.time.Period;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import dev.domain.DemandeAbsence;
import dev.domain.enums.Status;
import dev.domain.enums.Type;

@Configuration
@EnableScheduling
public class TraitementNuit {

	@Autowired
	DemandeAbsenceService serviceDemande;

	@Scheduled(cron="0 23 * * * *")
//	@Scheduled(initialDelay=10000, fixedDelay=50000) //Ne pas supprimer
	public void traitementNocturne() {

		System.out.println("Avant traitement");
		List<DemandeAbsence> demandesATraiter = this.serviceDemande.listeDemandesInitialesTN();

		for (DemandeAbsence demande : demandesATraiter) {
			
			System.out.println("Durant traitement ");
			
			// Pour les congés payés
			if (demande.getType().equals(Type.CONGES_PAYES)){
				
				System.out.println("congés payés");
				if(demande.getCollegueConcerne().getSoldeCongesPayes() >= Period.between(demande.getDateDebut(), demande.getDateFin()).getDays()) {
				
					
					demande.setStatus(Status.EN_ATTENTE_VALIDATION);
					demande.getCollegueConcerne().setSoldeCongesPayes(demande.getCollegueConcerne().getSoldeCongesPayes()
							- Period.between(demande.getDateDebut(), demande.getDateFin()).getDays());
					
					serviceDemande.sauvegarderModifDemandesTN(demande);
					
				} else {
					
					demande.setStatus(Status.REJETEE);
					
					serviceDemande.sauvegarderModifDemandesTN(demande);
					
				}
			}
			

			// Pour les RTT
			if(demande.getType().equals(Type.RTT)){
				
				if (demande.getCollegueConcerne().getSoldeRTT() >= Period
						.between(demande.getDateDebut(), demande.getDateFin()).getDays()) {
					
					
					demande.setStatus(Status.EN_ATTENTE_VALIDATION);
					demande.getCollegueConcerne().setSoldeRTT(demande.getCollegueConcerne().getSoldeRTT()
							- Period.between(demande.getDateDebut(), demande.getDateFin()).getDays());
					
					serviceDemande.sauvegarderModifDemandesTN(demande);
					
				} else {
					
					demande.setStatus(Status.REJETEE);
					
					serviceDemande.sauvegarderModifDemandesTN(demande);
					
				}
					
			}

			// Pour les congés sans solde
			if(demande.getType().equals(Type.CONGES_SANS_SOLDE)) {
				
				if (demande.getCollegueConcerne().getSoldeCongesSansSolde() >= Period
						.between(demande.getDateDebut(), demande.getDateFin()).getDays()) {
					
					demande.setStatus(Status.EN_ATTENTE_VALIDATION);
					
					demande.getCollegueConcerne().setSoldeCongesSansSolde(demande.getCollegueConcerne().getSoldeCongesSansSolde()
							- Period.between(demande.getDateDebut(), demande.getDateFin()).getDays());
					
					serviceDemande.sauvegarderModifDemandesTN(demande);
					
					
				} else {
					
					demande.setStatus(Status.REJETEE);
					
					serviceDemande.sauvegarderModifDemandesTN(demande);
					
				}

			}

		}

	}

}
