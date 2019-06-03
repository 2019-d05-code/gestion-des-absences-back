package dev.services;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import com.mailjet.client.resource.Emailv31;

import dev.domain.DemandeAbsence;
import dev.domain.enums.Status;
import dev.domain.enums.Type;

/**
 * Traitement de nuit: les nouvelles demandes sont soit passées 
 * au status en attente de validation ou  rejetée de manière automatique
 * 
 * @author Nicolas
 *
 */
@Configuration
@EnableScheduling
public class TraitementNuit {

    @Value("${email.manager}")
    private String email;
    
    @Value("${publicKey}")
    private String pub;
    
    @Value("${privateKey}")
    private String priv;
    
	MailjetClient client;
    MailjetRequest request;
    MailjetResponse response;
	
    String notification;
    
	@Autowired
	DemandeAbsenceService serviceDemande;

	//@Scheduled(cron="0 0 23 * * *")
	@Scheduled(initialDelay=10000, fixedDelay=120000) //Ne pas supprimer
	public void traitementNocturne() throws JSONException, MailjetException, MailjetSocketTimeoutException {
		
		client = new MailjetClient(pub, priv, new ClientOptions("v3.1"));
		

		List<DemandeAbsence> demandesATraiter = this.serviceDemande.listeDemandesInitialesTN();

		for (DemandeAbsence demande : demandesATraiter) {
			
			int decompteWeekends = TraitementNuit.recupCompteWeekends(demande.getDateDebut(), demande.getDateFin());
			
			// Pour les congés payés
			if (demande.getType().equals(Type.CONGES_PAYES)){
				
				if(demande.getCollegueConcerne().getSoldeCongesPayes() >= Period.between(demande.getDateDebut(), demande.getDateFin()).getDays()) {
				
					
					demande.setStatus(Status.EN_ATTENTE_VALIDATION);
					
					demande.getCollegueConcerne().setSoldeCongesPayes(demande.getCollegueConcerne().getSoldeCongesPayes()
							- decompteWeekends);
					
					serviceDemande.sauvegarderModifDemandesTN(demande);
					
					this.notification = "Vous avez une ou plusieurs demandes d'absence en attente de validation";
					
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
							- decompteWeekends);
					
					serviceDemande.sauvegarderModifDemandesTN(demande);
					
					this.notification = "Vous avez une ou plusieurs demandes d'absence en attente de validation";
					
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
							- decompteWeekends);
					
					serviceDemande.sauvegarderModifDemandesTN(demande);
					
					this.notification = "Vous avez une ou plusieurs demandes d'absence en attente de validation";
					
					
				} else {
					
					demande.setStatus(Status.REJETEE);
					
					serviceDemande.sauvegarderModifDemandesTN(demande);
					
				}

			}
			
			if(demande.getType().equals(Type.RTT_EMPLOYEUR)) {
				
				demande.getCollegueConcerne().setSoldeCongesSansSolde(demande.getCollegueConcerne().getSoldeRTT()
						- 1);
				
				demande.setStatus(Status.VALIDEE);
				
				serviceDemande.enregistrementDemandeRTTEmployeurTN(demande);
				
			}
			

		}

		if(this.notification != null){
			request = new MailjetRequest(Emailv31.resource)
					.property(Emailv31.MESSAGES, new JSONArray()
							.put(new JSONObject()
									.put(Emailv31.Message.FROM, new JSONObject()
											.put("Email", this.email)
											.put("Name", "Traitement de Nuit"))
									.put(Emailv31.Message.TO, new JSONArray()
											.put(new JSONObject()
													.put("Email", this.email)
													.put("Name", "Manager")))
									.put(Emailv31.Message.SUBJECT, "Notification demandes en attente")
									.put(Emailv31.Message.TEXTPART, "")
									.put(Emailv31.Message.HTMLPART, this.notification)));
			response = client.post(request);
			System.out.println(response.getStatus());
			System.out.println(response.getData());				
		}
		
	}
	
	/**
	 * Permet de calculer le nombre de jour durant la période analysée qui tombent un weekend
	 * 
	 * @param debut
	 * @param fin
	 * @return
	 */
	public static int recupCompteWeekends(LocalDate debut, LocalDate fin) {
		
		// Prise en compte des weekends dans le calcul du solde 
		List<LocalDate> weekends = new ArrayList<>();
		
		int periodeConges = Period.between(debut, fin).getDays();
		
		for(int i = 0; i < periodeConges; i++) {
			
			LocalDate plusDays = debut.plusDays(i);
			
			if(plusDays.getDayOfWeek().equals(DayOfWeek.SATURDAY) || plusDays.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
				weekends.add(plusDays);
			}
			
		}
		
		periodeConges -= weekends.size();
		
		return periodeConges;
		
	}

}
