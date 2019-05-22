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
        col2.setNom("Manager");
        col2.setPrenom("DEV");
        col2.setEmail("manager@dev.fr");
        col2.setMotDePasse(passwordEncoder.encode("superpass"));
        col2.setRoles(Arrays.asList(new RoleCollegue(col2, Role.ROLE_UTILISATEUR), new RoleCollegue(col2, Role.ROLE_MANAGER)));
        this.collegueRepo.save(col2);
        
        Collegue col3 = new Collegue();
        col3.setNom("User");
        col3.setPrenom("DEV");
        col3.setEmail("user@dev.fr");
        col3.setMotDePasse(passwordEncoder.encode("superpass"));
        col3.setRoles(Arrays.asList(new RoleCollegue(col3, Role.ROLE_UTILISATEUR)));
        this.collegueRepo.save(col3);
        
        //Création d'une demande validée pour tester le calendrier
        
        DemandeAbsence demTest = new DemandeAbsence();
        demTest.setDateDebut(LocalDate.of(2019, 5, 24));
        demTest.setDateFin(LocalDate.of(2019, 5, 28));
        demTest.setHeureCreation(LocalDateTime.now());
        demTest.setType(Type.RTT);
        demTest.setMotif("Julie");
        demTest.setStatus(Status.VALIDEE);
        demTest.setCollegueConcerne(col1);
        this.demandeRepo.save(demTest);
        
    }

}
