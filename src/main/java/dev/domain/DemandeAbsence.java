package dev.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import dev.controller.vm.DemandeAbsenceDTO;
import dev.controller.vm.MissionDTO;
import dev.domain.enums.Status;
import dev.domain.enums.Type;

/**
 * Entité représentant une demande d'absence,
 * elle est stockées en base de données en vue du traitement de nuit
 * 
 * @author Nicolas
 *
 */
@Entity
public class DemandeAbsence {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	/**
	 * Date du début de l'absence
	 */
	private LocalDate dateDebut;
	
	/**
	 * Date de la fin de l'absence
	 */
	private LocalDate dateFin;
	
	/**
	 * Heure de création de la demande
	 */
	private LocalDateTime heureCreation = LocalDateTime.now();
	
	/**
	 * Type d'absence
	 */
	@Enumerated(EnumType.STRING)
	private Type  type;
	
	/**
	 * Motif de l'absence
	 */
	private String motif;
	
	/**
	 * Status de la demande
	 */
	@Enumerated(EnumType.STRING)
	private Status status = Status.INITIALE;
	
	@ManyToOne
	@JoinColumn(name = "collegue_concerne")
	private Collegue collegueConcerne;

	public DemandeAbsence() {
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
		this.type = demande.getType();
		
		if(demande.getMotif() != null && demande.getMotif() != "") {
			this.motif = demande.getMotif();
		}
	}
	
	/**
	 * Constructeur permettant de générer une demande d'absence à partir d'une mission
	 * 
	 * @param mission
	 */
	public DemandeAbsence(MissionDTO mission) {
		this.id = mission.getId();
		this.dateDebut = this.getDateDebut();
		this.dateFin = mission.getDateFin();
		this.motif = mission.getNature();
		this.status = mission.getStatut();
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

	/**
	 * @return the collegueConcerne
	 */
	public Collegue getCollegueConcerne() {
		return collegueConcerne;
	}

	/**
	 * @param collegueConcerne the collegueConcerne to set
	 */
	public void setCollegueConcerne(Collegue collegueConcerne) {
		this.collegueConcerne = collegueConcerne;
	}
	
}
