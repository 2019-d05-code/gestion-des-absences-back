package dev.controller.vm;

import dev.domain.DemandeAbsence;

/**
 * Correspond à une demande en attente de validation par le manager (elle contient l'identitée du demandeur)
 * 
 * @author Nicolas
 *
 */
public class DemandeAbsenceValidationDTO extends DemandeAbsenceDTO {

	/**
	 * Nom du collègue qui demande l'absence
	 */
	String nom;
	
	/**
	 * Prénom du collegue qui demande l'absence
	 */
	String prenom;
	
	public DemandeAbsenceValidationDTO(){
		/**
		 * Constructeur par défaut
		 */
	}
	
	public DemandeAbsenceValidationDTO(DemandeAbsence demande) {
		super(demande);
		this.nom = demande.getCollegueConcerne().getNom();
		this.prenom = demande.getCollegueConcerne().getPrenom();
	}
	
	public DemandeAbsenceValidationDTO(DemandeAbsenceDTO demande, String nom, String prenom) {
		this.setDateDebut(demande.getDateDebut());
		this.setDateFin(demande.getDateFin());
		this.setMotif(demande.getMotif());
		this.setStatus(demande.getStatus());
		this.setEmail(demande.getEmail());
		this.setId(demande.getId());
		this.setType(demande.getType());
		this.nom = nom;
		this.prenom = prenom;
	}

	/**
	 * @return the nom
	 */
	public String getNom() {
		return nom;
	}

	/**
	 * @param nom the nom to set
	 */
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	 * @return the prenom
	 */
	public String getPrenom() {
		return prenom;
	}

	/**
	 * @param prenom the prenom to set
	 */
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	
	
	
}
