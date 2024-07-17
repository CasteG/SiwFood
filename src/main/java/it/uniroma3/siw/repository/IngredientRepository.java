package it.uniroma3.siw.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Ingredient;

public interface IngredientRepository extends CrudRepository<Ingredient,Long> {

	public boolean existsByName(String name);


}
