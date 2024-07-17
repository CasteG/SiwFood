package it.uniroma3.siw.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Ingredient;
import it.uniroma3.siw.model.RecipeIngredient;
import it.uniroma3.siw.repository.IngredientRepository;

@Service
public class IngredientService {
	
	@Autowired
	private IngredientRepository ingredientRepository;

	public Iterable<Ingredient> findAll() {
		return this.ingredientRepository.findAll();
	}

	public Ingredient findById(Long id) {
		return this.ingredientRepository.findById(id).get();
	}

	public void save(Ingredient ingredient) {
		this.ingredientRepository.save(ingredient);
	}
	
	/* restituisce solo gli ingredienti che non sono presenti nella lista passata */
	public Set<Ingredient> findAvailableIngredients(Set<RecipeIngredient> recipeIngredients) {
		
		Set<Ingredient> availableIngredients = new HashSet<>();
		Set<Ingredient> recipeIngredientsSet = new HashSet<>();
		
		
		/* creo la lista di oggetti di tipo Ingrediente della ricetta 
		 * prendendoli dall'entità RecipeIngredient */
		for(RecipeIngredient recipeIngredient : recipeIngredients) {
			recipeIngredientsSet.add(recipeIngredient.getIngredient());
		}
        
		/* per ogni ingrediente trovato nel sistema, se non è contenuto nella 
		 * lista di ingredienti della ricetta allora aggiungilo alla lista 
		 * di ingredienti disponibili  */
		for (Ingredient ingredient : this.ingredientRepository.findAll()) {
			if (!recipeIngredientsSet.contains(ingredient)) {
				availableIngredients.add(ingredient);
			}
		}
		
        return availableIngredients;
	}

	public void remove(Ingredient ingredient) {
		this.ingredientRepository.delete(ingredient);
	}

	

}
