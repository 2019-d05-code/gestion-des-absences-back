package dev.controller.vm;

import java.util.List;
import java.util.stream.Collectors;

import dev.domain.Departement;

/**
 * Représente un département au sein de l'entreprise
 * Cette entité est destinée à être renvoyé côté front
 * 
 * @author Nicolas
 *
 */
public class DepartementDTO {

	private long id;
	
	private String nom;
	
	private String emailManager;
	
	private List<Long> collegues;
	
	public DepartementDTO() {
		/**
		 * Constructeur par défaut
		 */
	}
	
	public DepartementDTO(Departement departement) {
		this.id = departement.getId();
		this.nom = departement.getNom();
		this.emailManager = departement.getManager().getEmail();
		this.collegues = departement.getCollegues().stream().map(collegue -> collegue.getId()).collect(Collectors.toList());
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
	 * @return the emailManager
	 */
	public String getEmailManager() {
		return emailManager;
	}

	/**
	 * @param emailManager the emailManager to set
	 */
	public void setEmailManager(String emailManager) {
		this.emailManager = emailManager;
	}

	/**
	 * @return the collegues
	 */
	public List<Long> getCollegues() {
		return collegues;
	}

	/**
	 * @param collegues the collegues to set
	 */
	public void setCollegues(List<Long> collegues) {
		this.collegues = collegues;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}
	
}
