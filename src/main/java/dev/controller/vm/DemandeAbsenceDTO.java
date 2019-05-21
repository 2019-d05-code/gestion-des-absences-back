package dev.controller.vm;

import java.time.LocalDate;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotEmpty;

import dev.domain.enums.Type;

/**
 * Correspond à une demande d'absence reçue depuis l'application côté front
 * 
 * @author Nicolas
 *
 */
public class DemandeAbsenceDTO {

	/**
	 * date du début de l'absence
	 */
	@NotEmpty(message = "La date de début de l'absence doit être obligatoirement renseignée")
	@Future(message = "Une demande d'absence ne peut concerner d'une période future")
	private LocalDate dateDebut;

	/**
	 * date de la fin de l'absence
	 */
	@NotEmpty(message = "La date de fin de l'absence doit être obligatoirement renseignée")
	@Future(message = "Une demande d'absence ne peut concerner d'une période future")
	private LocalDate dateFin;

	/**
	 * type d'absence
	 */
	private Type type;

	/**
	 * motif de l'absence
	 */
	private String motif;

	/**
	 * @return the dateDebut
	 */
	public LocalDate getDateDebut() {
		return dateDebut;
	}

	/**
	 * @param dateDebut the dateDebut to set
	 */
	public void setDateDebut(LocalDate dateDebut) {
		this.dateDebut = dateDebut;
	}

	/**
	 * @return the dateFin
	 */
	public LocalDate getDateFin() {
		return dateFin;
	}

	/**
	 * @param dateFin the dateFin to set
	 */
	public void setDateFin(LocalDate dateFin) {
		this.dateFin = dateFin;
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

	/**
	 * @return the motif
	 */
	public String getMotif() {
		return motif;
	}

	/**
	 * @param motif the motif to set
	 */
	public void setMotif(String motif) {
		this.motif = motif;
	}

}
