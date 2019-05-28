package dev.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Entité représentant un département au sein de l'entreprise
 * 
 * @author Nicolas
 *
 */
@Entity
@Table(name = "departement")
public class Departement {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String nom;
	
	@OneToOne
	private Collegue manager;
	
	@OneToMany(mappedBy = "departement")
	private List<Collegue> collegues;
	
	public Departement() {
		/**
		 * Constructeur par défaut
		 */
	}
	
	public Departement(String nom, Collegue manager) {
		this.nom = nom;
		this.manager = manager;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
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
	 * @return the manager
	 */
	public Collegue getManager() {
		return manager;
	}

	/**
	 * @param manager the manager to set
	 */
	public void setManager(Collegue manager) {
		this.manager = manager;
	}

	/**
	 * @return the collegues
	 */
	public List<Collegue> getCollegues() {
		return collegues;
	}

	/**
	 * @param collegues the collegues to set
	 */
	public void setCollegues(List<Collegue> collegues) {
		this.collegues = collegues;
	}
	
}
