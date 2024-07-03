package it.uniroma3.siw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Chef;
import it.uniroma3.siw.model.Recipe;
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
		return this.recipeRepository.findByName(name);		
	}

	public void remove(Recipe recipe) {
		this.recipeRepository.delete(recipe);
	}

	public Iterable<Recipe> findByChef(String firstName, String lastName) {
		return this.recipeRepository.findByChefFirstNameAndChefLastName(firstName,lastName);
	}
}
