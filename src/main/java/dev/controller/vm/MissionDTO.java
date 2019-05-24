package dev.controller.vm;

import java.time.LocalDate;

import dev.domain.Mission;
import dev.domain.enums.Status;

/**
 * Entité DTO représentant une mission
 * 
 * @author Nicolas
 *
 */
public class MissionDTO {
	
	private int id;

	private LocalDate dateDebut;

	private LocalDate dateFin;

	private String nature;

	private String villeDepart;

	private String villeArrivee;

	private String transport;

	private Status statut;

	public MissionDTO() {
		/**
		 * Constructeur par défaut
		 */
	}

	public MissionDTO(Mission mission) {
		this.id = mission.getId();
		this.dateDebut = mission.getDateDebut();
		this.dateFin = mission.getDateFin();
		this.nature = mission.getNature();
		this.villeDepart = mission.getVilleDepart();
		this.villeArrivee = mission.getVilleArrivee();
		this.transport = mission.getTransport();
		this.statut = mission.getStatut();
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the dateDebut
	 */
	public LocalDate getDateDebut() {
		return dateDebut;
	}

	/**
	 * @param dateDebut
	 *            the dateDebut to set
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
	 * @param dateFin
	 *            the dateFin to set
	 */
	public void setDateFin(LocalDate dateFin) {
		this.dateFin = dateFin;
	}

	/**
	 * @return the nature
	 */
	public String getNature() {
		return nature;
	}

	/**
	 * @param nature
	 *            the nature to set
	 */
	public void setNature(String nature) {
		this.nature = nature;
	}

	/**
	 * @return the villeDepart
	 */
	public String getVilleDepart() {
		return villeDepart;
	}

	/**
	 * @param villeDepart
	 *            the villeDepart to set
	 */
	public void setVilleDepart(String villeDepart) {
		this.villeDepart = villeDepart;
	}

	/**
	 * @return the statut
	 */
	public Status getStatut() {
		return statut;
	}

	/**
	 * @param statut
	 *            the statut to set
	 */
	public void setStatut(Status statut) {
		this.statut = statut;
	}

	/**
	 * @return the villeArrivee
	 */
	public String getVilleArrivee() {
		return villeArrivee;
	}

	/**
	 * @param villeArrivee
	 *            the villeArrivee to set
	 */
	public void setVilleArrivee(String villeArrivee) {
		this.villeArrivee = villeArrivee;
	}

	/**
	 * @return the transport
	 */
	public String getTransport() {
		return transport;
	}

	/**
	 * @param transport
	 *            the transport to set
	 */
	public void setTransport(String transport) {
		this.transport = transport;
	}
}
