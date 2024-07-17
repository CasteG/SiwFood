package it.uniroma3.siw.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Ingredient;
import it.uniroma3.siw.repository.IngredientRepository;

@Component
public class IngredientValidator implements Validator {
	
	@Autowired
	private IngredientRepository ingredientRepository;
	
	@Override
	public void validate(Object o, Errors errors) {
		Ingredient ing = (Ingredient) o;
		
		if(ing.getName()!=null && this.ingredientRepository.existsByName(ing.getName())) {
			errors.reject("ingredient.duplicate");
		}
		
	}

	@Override
		public boolean supports(Class<?> aClass) {
			return Ingredient.class.equals(aClass);
		}

}
