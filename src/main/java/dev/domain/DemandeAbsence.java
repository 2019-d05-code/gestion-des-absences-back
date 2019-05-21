package dev.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotEmpty;

import dev.controller.vm.DemandeAbsenceDTO;
import dev.domain.enums.Status;
import dev.domain.enums.Type;

/**
 * Entité représentatn une demande d'absence,
 * elle est stockées en base de données en vue du traitement de nuit
 * 
 * @author Nicolas
 *
 */
@Entity
public class DemandeAbsence {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	/**
	 * Date du début de l'absence
	 */
	@NotEmpty(message = "La date de début de l'absence doit être obligatoirement renseignée")
	@Future(message = "Une demande d'absence ne peut concerner d'une période future")
	private LocalDate dateDebut;
	
	/**
	 * Date de la fin de l'absence
	 */
	@NotEmpty(message = "La date de fin de l'absence doit être obligatoirement renseignée")
	@Future(message = "Une demande d'absence ne peut concerner d'une période future")
	private LocalDate dateFin;
	
	/**
	 * Heure de création de la demande
	 */
	@NotEmpty(message = "La date de création doit être obligatoirement renseignée")
	private LocalDateTime heureCreation = LocalDateTime.now();
	
	/**
	 * Type d'absence
	 */
	@Enumerated
	@NotEmpty(message = "Le type de demande d'absence doit être obligatoirement renseigné")
	private Type  type;
	
	/**
	 * Motif de l'absence
	 */
	private String motif;
	
	/**
	 * Status de la demande
	 */
	@Enumerated
	@NotEmpty(message = "Une demande d'absence a obligatoirement un status")
	private Status status = Status.INITIALE;

	DemandeAbsence() {
		/**
		 * Constructeur par défaut
		 */
	}
	
	/**
	 * Constructeur permettant de transformer un demande DTO en entité demande
	 * 
	 * @param demande
	 */
	public DemandeAbsence(@Valid DemandeAbsenceDTO demande) {
		this.dateDebut = demande.getDateDebut();
		this.dateFin = demande.getDateFin();
		this.motif = demande.getMotif();
		this.type = demande.getType();
	}
	
	/**
	 * @return the status
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Status status) {
		this.status = status;
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

	/**
	 * @return the heureCreation
	 */
	public LocalDateTime getHeureCreation() {
		return heureCreation;
	}

	/**
	 * @param heureCreation the heureCreation to set
	 */
	public void setHeureCreation(LocalDateTime heureCreation) {
		this.heureCreation = heureCreation;
	}
	
}
