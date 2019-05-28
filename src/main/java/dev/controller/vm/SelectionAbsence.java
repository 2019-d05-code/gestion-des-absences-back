package dev.controller.vm;

public class SelectionAbsence {
	
	public SelectionAbsence(){}

	public SelectionAbsence(Integer mois, Integer annee, Long departement) {
		super();
		this.mois = mois;
		this.annee = annee;
		this.departement = departement;
	}

	private Integer mois;
	private Integer annee;
	private Long departement;

	public Integer getMois() {
		return mois;
	}

	public void setMois(Integer mois) {
		this.mois = mois;
	}

	public Integer getAnnee() {
		return annee;
	}

	public void setAnnee(Integer annee) {
		this.annee = annee;
	}

	public Long getDepartement() {
		return departement;
	}

	public void setDepartement(Long departement) {
		this.departement = departement;
	}

}
