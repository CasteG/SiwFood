package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.model.Ingredient;
import it.uniroma3.siw.service.IngredientService;
import it.uniroma3.siw.validator.IngredientValidator;
import jakarta.validation.Valid;

@Controller
public class IngredientController {

	@Autowired
	private IngredientService ingredientService;
	
	@Autowired 
	private IngredientValidator ingredientValidator;
	
	@GetMapping("/ingredient")
	public String showIngredients(Model model) {
		model.addAttribute("ingredients", ingredientService.findAll());
		return "ingredients.html";
	}
	
	@GetMapping("/ingredient/{id}")
	public String getIngredient(@PathVariable("id") Long id, Model model) {
		model.addAttribute("ingredient", ingredientService.findById(id));
		return "ingredient.html";
	}
	
	@GetMapping("/admin/manageIngredients")
	public String manageIngredients(Model model) {
		model.addAttribute("ingredients", this.ingredientService.findAll());
		return "admin/manageIngredients.html";
	}
	
	@GetMapping("/admin/removeIngredient/{id}")
	public String removeIngredient(@PathVariable("id") Long id, Model model) {
		this.ingredientService.remove(this.ingredientService.findById(id));
		return "admin/successfulRemoval.html";
	}
	
	@GetMapping("/admin/formNewIngredient")
	public String formNewIngredient(Model model) {
		model.addAttribute("ingredient", new Ingredient());
		return "admin/formNewIngredient.html";
	}
	
	@PostMapping("/ingredient")
	public String newIngredient(@Valid @ModelAttribute("ingredient") Ingredient ingredient,
			BindingResult bindingResult, Model model) {
		
		this.ingredientValidator.validate(ingredient, bindingResult);
		if(!bindingResult.hasErrors()) {
			this.ingredientService.save(ingredient);
			model.addAttribute("ingredient", ingredient);
			return "redirect:ingredient";
		}
		else {
			model.addAttribute("ingredient", ingredient);
			return "admin/formNewIngredient.html";
		}
	}
	
}


