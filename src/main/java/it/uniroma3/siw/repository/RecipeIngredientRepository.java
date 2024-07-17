package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Ingredient;
import it.uniroma3.siw.model.Recipe;
import it.uniroma3.siw.model.RecipeIngredient;

public interface RecipeIngredientRepository extends CrudRepository<RecipeIngredient, Long> {

	public List<RecipeIngredient> findByRecipe(Recipe recipe);

	public void deleteByIngredientId(Long id);

	public List<RecipeIngredient> findByIngredient(Ingredient ingredient);

}
