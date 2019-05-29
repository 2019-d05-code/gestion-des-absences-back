package dev.controller.vm;

import java.util.List;

/**
 * Entité représentant les week-ends avec les absences de tous les employés A
 * utiliser dans le rapport
 * 
 * @author Julie
 *
 */

public class RapportAbsences {

	private List<Integer> joursWeekEnd;

	private List<Absences> listeAbsences;

	public RapportAbsences() {

	}

	public RapportAbsences(List<Integer> joursWeekEnd, List<Absences> listeAbsences) {
		this.joursWeekEnd = joursWeekEnd;
		this.listeAbsences = listeAbsences;
	}

	public List<Integer> getJoursWeekEnd() {
		return joursWeekEnd;
	}

	public void setJoursWeekEnd(List<Integer> joursWeekEnd) {
		this.joursWeekEnd = joursWeekEnd;
	}

	public List<Absences> getListeAbsences() {
		return listeAbsences;
	}

	public void setListeAbsences(List<Absences> listeAbsences) {
		this.listeAbsences = listeAbsences;
	}

}
