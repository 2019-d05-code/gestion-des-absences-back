package dev;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import dev.domain.Collegue;
import dev.domain.DemandeAbsence;
import dev.domain.RoleCollegue;
import dev.domain.Version;
import dev.domain.enums.Role;
import dev.domain.enums.Status;
import dev.domain.enums.Type;
import dev.repository.CollegueRepo;
import dev.repository.DemandeAbsenceRepo;
import dev.repository.VersionRepo;

/**
 * Code de démarrage de l'application.
 * Insertion de jeux de données.
 */
@Component
public class StartupListener {

    private String appVersion;
    private VersionRepo versionRepo;
    private PasswordEncoder passwordEncoder;
    private CollegueRepo collegueRepo;
    private DemandeAbsenceRepo demandeRepo;

    public StartupListener(@Value("${app.version}") String appVersion, VersionRepo versionRepo, PasswordEncoder passwordEncoder, CollegueRepo collegueRepo, DemandeAbsenceRepo demandeRepo) {
        this.appVersion = appVersion;
        this.versionRepo = versionRepo;
        this.passwordEncoder = passwordEncoder;
        this.collegueRepo = collegueRepo;
        this.demandeRepo = demandeRepo;
    }

    @EventListener(ContextRefreshedEvent.class)
    public void onStart() {
        this.versionRepo.save(new Version(appVersion));

        // Création de trois utilisateurs

        Collegue col1 = new Collegue();
        col1.setNom("Admin");
        col1.setPrenom("DEV");
        col1.setEmail("admin@dev.fr");
        col1.setMotDePasse(passwordEncoder.encode("superpass"));
        col1.setRoles(Arrays.asList(new RoleCollegue(col1, Role.ROLE_ADMINISTRATEUR), new RoleCollegue(col1, Role.ROLE_UTILISATEUR)));
        this.collegueRepo.save(col1);

        Collegue col2 = new Collegue();
        col2.setNom("User");
        col2.setPrenom("DEV");
        col2.setEmail("user@dev.fr");
        col2.setMotDePasse(passwordEncoder.encode("superpass"));
        col2.setRoles(Arrays.asList(new RoleCollegue(col2, Role.ROLE_UTILISATEUR)));
        this.collegueRepo.save(col2);
        
        Collegue col3 = new Collegue();
        col3.setNom("Manager");
        col3.setPrenom("DEV");
        col3.setEmail("manager@dev.fr");
        col3.setMotDePasse(passwordEncoder.encode("superpass"));
        col3.setRoles(Arrays.asList(new RoleCollegue(col3, Role.ROLE_UTILISATEUR), new RoleCollegue(col2, Role.ROLE_MANAGER)));
        col3.setSoldeCongesSansSolde(10);
        this.collegueRepo.save(col3);
        
        //Création de demandes validées pour tester le calendrier
        
        DemandeAbsence demTest = new DemandeAbsence();
        demTest.setDateDebut(LocalDate.of(2019, 5, 24));
        demTest.setDateFin(LocalDate.of(2019, 5, 28));
        demTest.setHeureCreation(LocalDateTime.now());
        demTest.setType(Type.RTT);
        demTest.setMotif("Julie");
        demTest.setStatus(Status.VALIDEE);
        demTest.setCollegueConcerne(col1);
        this.demandeRepo.save(demTest);
        
        DemandeAbsence demTest2 = new DemandeAbsence();
        demTest2.setDateDebut(LocalDate.of(2019, 9, 5));
        demTest2.setDateFin(LocalDate.of(2019, 9, 20));
        demTest2.setHeureCreation(LocalDateTime.now());
        demTest2.setType(Type.CONGES_PAYES);
        demTest2.setMotif("Katrina");
        demTest2.setStatus(Status.VALIDEE);
        demTest2.setCollegueConcerne(col1);
        this.demandeRepo.save(demTest2);
        
        // Création de demandes d'absences test pour le traitement de nuit
        
        DemandeAbsence demandeEnAttente1 = new DemandeAbsence();
        demandeEnAttente1.setDateDebut(LocalDate.now().plusMonths(1));
        demandeEnAttente1.setDateFin(LocalDate.now().plusMonths(1).plusDays(5));
        demandeEnAttente1.setHeureCreation(LocalDateTime.now());
        demandeEnAttente1.setType(Type.RTT);
        demandeEnAttente1.setStatus(Status.INITIALE);
        demandeEnAttente1.setCollegueConcerne(col1);
        this.demandeRepo.save(demandeEnAttente1);
        
        DemandeAbsence demandeEnAttente2 = new DemandeAbsence();
        demandeEnAttente2.setDateDebut(LocalDate.now().plusDays(10));
        demandeEnAttente2.setDateFin(LocalDate.now().plusDays(18));
        demandeEnAttente2.setHeureCreation(LocalDateTime.now());
        demandeEnAttente2.setType(Type.CONGES_PAYES);
        demandeEnAttente2.setMotif("Vacances à la mer");
        demandeEnAttente2.setStatus(Status.INITIALE);
        demandeEnAttente2.setCollegueConcerne(col2);
        this.demandeRepo.save(demandeEnAttente2);
        
        DemandeAbsence demandeEnAttente3 = new DemandeAbsence();
        demandeEnAttente3.setDateDebut(LocalDate.now().plusDays(20));
        demandeEnAttente3.setDateFin(LocalDate.now().plusDays(25));
        demandeEnAttente3.setHeureCreation(LocalDateTime.now());
        demandeEnAttente3.setType(Type.CONGES_SANS_SOLDE);
        demandeEnAttente3.setMotif("Raisons familiales");
        demandeEnAttente3.setStatus(Status.INITIALE);
        demandeEnAttente3.setCollegueConcerne(col3);
        this.demandeRepo.save(demandeEnAttente3);
        
    }

}
