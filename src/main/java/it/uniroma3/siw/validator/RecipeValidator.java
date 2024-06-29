package it.uniroma3.siw.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Recipe;
import it.uniroma3.siw.repository.RecipeRepository;

@Component
public class RecipeValidator implements Validator {
	
	@Autowired
	private RecipeRepository recipeRepository;
	
	@Override
	public void validate(Object o, Errors errors) {
		Recipe recipe = (Recipe) o;
		if(recipe.getName()!=null && recipe.getChef()!=null
			&& recipeRepository.existsByNameAndChef(recipe.getName(), recipe.getChef())) {
			errors.reject("recipe.duplicate");
		}
	}
	
	/* sezione standard di codice che indica che sto 
	 * lavorando sulla classe Recipe */
	@Override
	public boolean supports(Class<?> aClass) {
		return Recipe.class.equals(aClass);
	}


}
