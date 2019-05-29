package dev.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.controller.vm.DepartementDTO;
import dev.repository.DepartementRepo;

@Service
public class DepartementService {

	@Autowired
	DepartementRepo depRepo;

	/**
	 * Retourne la liste des départements
	 * 
	 * @return List<DepartementDTO>
	 */
	public List<DepartementDTO> recupererDepartements() {

		return depRepo.findAll().stream().map(departement -> new DepartementDTO(departement))
				.collect(Collectors.toList());
	}

}
