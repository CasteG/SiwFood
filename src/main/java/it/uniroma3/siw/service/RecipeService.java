package it.uniroma3.siw.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.model.Chef;
import it.uniroma3.siw.model.Recipe;
import it.uniroma3.siw.model.RecipeIngredient;
import it.uniroma3.siw.repository.RecipeRepository;

@Service
public class RecipeService {

	@Autowired
	private RecipeRepository recipeRepository;
	
	public void save(Recipe recipe) {
		this.recipeRepository.save(recipe);
	}

	public boolean existsByNameAndChef(String nome, Chef chef) {
		return this.recipeRepository.existsByNameAndChef(nome, chef);
	}

	public Recipe findById(Long id) {
		return this.recipeRepository.findById(id).get();
	}

	public Iterable<Recipe> findAll() {
		return this.recipeRepository.findAll();
	}
	
	/* Iterable perchè a un nome possono corrispondere più ricette,
	 * a patto che siano create da cuochi diversi */
	public Iterable<Recipe> findByName(String name) {
		return this.recipeRepository.findByNameStartingWith(name);
	}

	public void remove(Recipe recipe) {
		this.recipeRepository.delete(recipe);
	}

	public Iterable<Recipe> findByChef(String firstName, String lastName) {
		return this.recipeRepository.findByChefFirstNameAndChefLastName(firstName,lastName);
	}

	public Set<Recipe> findByChefId(Long byId) {
		return this.recipeRepository.findByChefId(byId);
	}

	@Transactional
    public void updateRecipeIngredients(Long idRecipe, List<Long> ingredientIds, List<Integer> quantities, List<String> units) {
        Recipe recipe = this.findById(idRecipe);
        /* per ogni ingrediente prendo l'oggetto ingrediente corrispondente all'id */
        for (int i = 0; i < ingredientIds.size(); i++) {
            Long ingredientId = ingredientIds.get(i);
            //prendo la quantità
            Integer quantity = quantities.get(i);
            //prendo l'unità di misura
            String unit = units.get(i);
            //trasforma la lista recipeIngredient in uno stream (seq di elementi che supporta varie operazioni
            //applicabili in modo sequenziale e parallelo)
            RecipeIngredient recipeIngredient = recipe.getRecipeIngredients().stream()
            		//filter:seleziono solo gli elementi dello stream (ingredienti) che soddisfano una certa condizione
                    .filter(ri -> ri.getIngredient().getId().equals(ingredientId))
                    //prende il primo elemento che soddisfa la condizione
                    .findFirst()
                    //se non viene trovato alcun elemento
                    .orElseThrow(() -> new IllegalArgumentException("Invalid ingredient Id:" + ingredientId));
            //aggiorno quantità e unità
            recipeIngredient.setQuantity(quantity);
            recipeIngredient.setUnit(unit);
        }
        recipeRepository.save(recipe);
    }

	public List<Recipe> findByNameStartingWith(String prefix) {
		return this.recipeRepository.findByNameStartingWith(prefix);
	}
}
