package dev.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import dev.domain.JourFerie;

@Repository
public interface JourFerieRepository extends JpaRepository<JourFerie, Long> {

	@Query("select j from JourFerie j where j.date = :dateJF")
	public JourFerie findJourFerie(@Param("dateJF") LocalDate dateJF);
	
}
