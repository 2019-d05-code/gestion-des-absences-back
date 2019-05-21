package dev.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.domain.DemandeAbsence;

@Repository
public interface DemandeAbsenceRepo extends JpaRepository<DemandeAbsence, Long> {

}
