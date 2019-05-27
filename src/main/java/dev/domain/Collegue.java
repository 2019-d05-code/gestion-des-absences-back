package dev.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Collegue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    private String prenom;

    private String email;

    private String motDePasse;
    
    private Integer soldeRTT = 10;
    
    private Integer soldeCongesPayes = 25;
    
    private Integer soldeCongesSansSolde = 0;
    
    @ManyToOne
    @JoinColumn(name = "departement_id")
    private Departement departement;

    @OneToMany(mappedBy = "collegue", cascade = CascadeType.PERSIST)
    private List<RoleCollegue> roles;
    
    @OneToMany(mappedBy = "collegueConcerne")
    private List<DemandeAbsence> demandesAbsence;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public List<RoleCollegue> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleCollegue> roles) {
        this.roles = roles;
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

	/**
	 * @return the demandesAbsence
	 */
	public List<DemandeAbsence> getDemandesAbsence() {
		return demandesAbsence;
	}

	/**
	 * @param demandesAbsence the demandesAbsence to set
	 */
	public void setDemandesAbsence(List<DemandeAbsence> demandesAbsence) {
		this.demandesAbsence = demandesAbsence;
	}

	/**
	 * @return the departement
	 */
	public Departement getDepartement() {
		return departement;
	}

	/**
	 * @param departement the departement to set
	 */
	public void setDepartement(Departement departement) {
		this.departement = departement;
	}
}
