package dev.controller.vm;

import java.util.List;

/**
 * Classe qui récupère les tableaux de jours de RTT, congés et CSS par collègue
 * 
 * @author julie
 *
 */
public class Absences {

	private String nomCollegue;

	private String prenomCollegue;

	/** Tableau qui comprennent les jours où un RTT/Congé/CSS est posé par un collègue dans un mois donné
	 * 
	 */
	private List<Integer> joursRTT;
	private List<Integer> joursCP;
	private List<Integer> joursCSS;
	private List<Integer> joursMISSIONS;

	public Absences(String nomCollegue, String prenomCollegue, List<Integer> joursRTT, List<Integer> joursCP,
			List<Integer> joursCSS) {
		this.nomCollegue = nomCollegue;
		this.prenomCollegue = prenomCollegue;
		this.joursRTT = joursRTT;
		this.joursCP = joursCP;
		this.joursCSS = joursCSS;

	}
	
	public Absences(String nomCollegue, String prenomCollegue, List<Integer> joursMISSIONS) {
		this.nomCollegue = nomCollegue;
		this.prenomCollegue = prenomCollegue;
		this.joursMISSIONS = joursMISSIONS;

	}



	public String getPrenomCollegue() {
		return prenomCollegue;
	}

	public void setPrenomCollegue(String prenomCollegue) {
		this.prenomCollegue = prenomCollegue;
	}

	public List<Integer> getJoursRTT() {
		return joursRTT;
	}

	public void setJoursRTT(List<Integer> joursRTT) {
		this.joursRTT = joursRTT;
	}

	public List<Integer> getJoursCP() {
		return joursCP;
	}

	public void setJoursCP(List<Integer> joursCP) {
		this.joursCP = joursCP;
	}

	public List<Integer> getJoursCSS() {
		return joursCSS;
	}

	public void setJoursCSS(List<Integer> joursCSS) {
		this.joursCSS = joursCSS;
	}

	public List<Integer> getJoursMISSIONS() {
		return joursMISSIONS;
	}

	public void setJoursMISSIONS(List<Integer> joursMISSIONS) {
		this.joursMISSIONS = joursMISSIONS;
	}



	/**
	 * @return the nomCollegue
	 */
	public String getNomCollegue() {
		return nomCollegue;
	}



	/**
	 * @param nomCollegue the nomCollegue to set
	 */
	public void setNomCollegue(String nomCollegue) {
		this.nomCollegue = nomCollegue;
	}

}
