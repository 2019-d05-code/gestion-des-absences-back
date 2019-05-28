package dev.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.domain.Departement;

/**
 * Repository pour la gestion CRUD des départements
 * 
 * @author Nicolas
 *
 */
@Repository
public interface DepartementRepo extends JpaRepository<Departement, Long> {
	
}
