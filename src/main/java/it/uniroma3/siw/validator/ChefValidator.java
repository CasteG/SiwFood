package it.uniroma3.siw.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Chef;
import it.uniroma3.siw.repository.ChefRepository;


@Component
public class ChefValidator implements Validator {
	
	@Autowired
	private ChefRepository chefRepository;
	
	@Override
	public void validate(Object o, Errors errors) {
		Chef chef = (Chef) o;
		if(chef.getFirstName()!=null && chef.getLastName()!=null
			&& chefRepository.existsByFirstNameAndLastNameAndDateOfBirth(chef.getFirstName(), chef.getLastName(), chef.getDateOfBirth())) {
			errors.reject("chef.duplicate");
		}
	}
	
	/* sezione standard di codice che indica che sto 
	 * lavorando sulla classe Recipe */
	@Override
	public boolean supports(Class<?> aClass) {
		return Chef.class.equals(aClass);
	}
	
}
