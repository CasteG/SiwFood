package it.uniroma3.siw.repository;

import java.time.LocalDate;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Chef;

public interface ChefRepository extends CrudRepository<Chef,Long> {
	
	public boolean existsByFirstNameAndLastNameAndDateOfBirth(String firstName, String lastName, LocalDate dateOfBirth);

	public Chef findByFirstNameAndLastName(String firstName, String lastName);

}
