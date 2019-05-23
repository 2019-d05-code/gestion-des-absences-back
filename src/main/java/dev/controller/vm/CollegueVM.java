package dev.controller.vm;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dev.domain.Collegue;
import dev.domain.enums.Role;

/**
 * Structure modèlisant un collègue servant à communiquer avec l'extérieur (WEB API).
 */
public class CollegueVM {

    private String email;
    private String nom;
    private String prenom;
    private List<Role> roles = new ArrayList<>();
    
	private Integer soldeRTT;

	private Integer soldeCongesPayes;

	private Integer soldeCongesSansSolde;

    public CollegueVM(Collegue col) {
        this.email = col.getEmail();
        this.nom = col.getNom();
        this.prenom = col.getPrenom();
        this.roles = col.getRoles().stream().map(roleCollegue -> roleCollegue.getRole()).collect(Collectors.toList());
        this.soldeRTT = col.getSoldeRTT();
        this.soldeCongesPayes = col.getSoldeCongesPayes();
        this.soldeCongesSansSolde = col.getSoldeCongesSansSolde();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

	/**
	 * @return the soldeRTT
	 */
	public Integer getSoldeRTT() {
		return soldeRTT;
	}

	/**
	 * @param soldeRTT the soldeRTT to set
	 */
	public void setSoldeRTT(Integer soldeRTT) {
		this.soldeRTT = soldeRTT;
	}

	/**
	 * @return the soldeCongesPayes
	 */
	public Integer getSoldeCongesPayes() {
		return soldeCongesPayes;
	}

	/**
	 * @param soldeCongesPayes the soldeCongesPayes to set
	 */
	public void setSoldeCongesPayes(Integer soldeCongesPayes) {
		this.soldeCongesPayes = soldeCongesPayes;
	}

	/**
	 * @return the soldeCongesSansSolde
	 */
	public Integer getSoldeCongesSansSolde() {
		return soldeCongesSansSolde;
	}

	/**
	 * @param soldeCongesSansSolde the soldeCongesSansSolde to set
	 */
	public void setSoldeCongesSansSolde(Integer soldeCongesSansSolde) {
		this.soldeCongesSansSolde = soldeCongesSansSolde;
	}
}
