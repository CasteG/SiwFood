package it.uniroma3.siw.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Chef;
import it.uniroma3.siw.model.Recipe;

public interface RecipeRepository extends CrudRepository<Recipe,Long> {
	
	public boolean existsByNameAndChef(String name, Chef chef);

	public Iterable<Recipe> findByName(String name);

	public Iterable<Recipe> findByChefFirstNameAndChefLastName(String firstName, String lastName);

	public Set<Recipe> findByChefId(Long byId);

	public boolean existsByName(String name);
	
	public List<Recipe> findByNameStartingWith(String prefix);
	
}
