package dev.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dev.controller.vm.DemandeAbsenceDTO;
import dev.domain.DemandeAbsence;

@Repository
public interface DemandeAbsenceRepo extends JpaRepository<DemandeAbsence, Long> {

	@Query("select d from DemandeAbsence d where d.status != 'REJETEE' and (d.dateDebut between :dateDebut and :dateFin) or (d.dateFin between :dateDebut and :dateFin)")
	public Optional<List<DemandeAbsence>> findConcurrentAbsence(@Param("dateDebut") LocalDate dateDebut, @Param("dateFin") LocalDate dateFin);
	
	public List<DemandeAbsence> findByCollegueConcerneEmail(String email); 
	
	@Query("select d from DemandeAbsence d where d.status = 'INITIALE'")
	public Optional<List<DemandeAbsence>> findByStatus();
	
	@Query("select d from DemandeAbsence d where d.collegueConcerne.email = :email")
	public Optional<List<DemandeAbsence>> findAllByEmail(@Param("email") String email);
	
	@Query("select d from DemandeAbsence d where d.collegueConcerne.email = :email and (d.dateDebut between :dateDebut and :dateFin)")
    public List<DemandeAbsenceDTO> findAbsencesParMois(@Param("email") String email, @Param("dateDebut") LocalDate dateDebut, @Param("dateFin") LocalDate dateFin);
	
}
