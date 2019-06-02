package dev.domain;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import dev.controller.vm.JourFerieDTO;
import dev.domain.enums.Type;

/**
 * Entité représentant un jour férié
 * 
 * @author Nicolas
 *
 */
@Entity
@Table(name = "jour_ferie")
public class JourFerie {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private LocalDate date;
	
	@Enumerated(EnumType.STRING)
	private Type type;
	
	@NotEmpty
	private String commentaire;

	public JourFerie() {
		/**
		 * Constructeur par défaut
		 */
	}
	
	public JourFerie(JourFerieDTO jourFerie) {
		if(jourFerie.getId() != null) {
			this.id = jourFerie.getId();			
		}
		this.date = jourFerie.getDate();
		this.commentaire = jourFerie.getCommentaire();
		this.type = jourFerie.getType();
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
	 * @return the date
	 */
	public LocalDate getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(LocalDate date) {
		this.date = date;
	}

	/**
	 * @return the commentaire
	 */
	public String getCommentaire() {
		return commentaire;
	}

	/**
	 * @param commentaire the commentaire to set
	 */
	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}

	/**
	 * @return the type
	 */
	public Type getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(Type type) {
		this.type = type;
	}
	
}
