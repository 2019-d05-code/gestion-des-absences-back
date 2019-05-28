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
