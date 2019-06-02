package dev.controller.vm;

import java.time.LocalDate;

import dev.domain.JourFerie;
import dev.domain.enums.Type;

/**
 * Entité DTO représentant un jour férié
 * 
 * @author Nicolas
 *
 */
public class JourFerieDTO {

	private Long id;
	
	private LocalDate date;
	
	private String commentaire;
	
	private Type type;
	
	public JourFerieDTO() {
		/**
		 * Constructeur par défaut
		 */
	}
	
	public JourFerieDTO(JourFerie jourFerie) {
		this.id = jourFerie.getId();
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
