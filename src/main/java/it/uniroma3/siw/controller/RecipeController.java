package it.uniroma3.siw.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Image;
import it.uniroma3.siw.model.Recipe;
import it.uniroma3.siw.repository.ImageRepository;
import it.uniroma3.siw.service.ChefService;
import it.uniroma3.siw.service.IngredientService;
import it.uniroma3.siw.service.RecipeIngredientService;
import it.uniroma3.siw.service.RecipeService;
import it.uniroma3.siw.validator.RecipeValidator;
import jakarta.validation.Valid;

@Controller
public class RecipeController {

	@Autowired
	private RecipeService recipeService;
	
	@Autowired
	private RecipeValidator recipeValidator;
	
	@Autowired
	private RecipeIngredientService recipeIngredientService;
	
	@Autowired
	private ChefService chefService;

	@Autowired
	private IngredientService ingredientService;
	
	@Autowired
	private ImageRepository imageRepository;
	
	@GetMapping("/formNewRecipe")
	public String formNewRecipe(Model model) {
		model.addAttribute("recipe", new Recipe());
		model.addAttribute("chefs", this.chefService.findAll());
		return "formNewRecipe.html";
	}

	@PostMapping("/recipe")
	public String newRecipe(@Valid @ModelAttribute("recipe") Recipe recipe, 
			BindingResult bindingResult, Model model,
			@RequestParam("file") MultipartFile image) throws IOException {
		
		this.recipeValidator.validate(recipe, bindingResult);
		if(bindingResult.hasErrors()) {
			model.addAttribute("recipe", recipe);
			model.addAttribute("chefs", this.chefService.findAll());
			return "formNewRecipe.html";
		}
		else {
			Image img = new Image(image.getBytes());
			this.imageRepository.save(img);
			recipe.setImage(img);
			this.recipeService.save(recipe); 
			model.addAttribute("recipe", recipe);
			return "redirect:recipe/"+recipe.getId();	//dico al client fammi una richiesta all'url recipe/{id} 
		}
	}

	@GetMapping("/recipe/{id}")
	public String getRecipe(@PathVariable("id") Long id, Model model) {
		Recipe recipe = this.recipeService.findById(id);
		model.addAttribute("recipe", recipe);
		return "recipe.html";
	}

	@GetMapping("/recipe")
	public String showRecipes(Model model) {
		model.addAttribute("recipes", this.recipeService.findAll());
		return "recipes.html";
	}

	@GetMapping("/formSearchRecipe")
	public String formSearchRecipes() {
		return "formSearchRecipe.html";
	}

	@PostMapping("/searchRecipe")
	public String searchRecipes(Model model, @RequestParam String name) {
		model.addAttribute("recipes", this.recipeService.findByName(name));
		return "recipes.html";
	}
	
	@GetMapping("/indexRecipe")
	public String indexRecipe(Model model) {
		return "indexRecipe.html";
	}
	
	@GetMapping("/manageRecipes")
	public String manageRecipes(Model model) {
		model.addAttribute("recipes", this.recipeService.findAll());
		return "manageRecipes.html";
	}
	
	@GetMapping("/formUpdateRecipe/{id}")
	public String updateRecipe(@PathVariable("id") Long id, Model model) {
		model.addAttribute("recipe", this.recipeService.findById(id));
		return "formUpdateRecipe.html";
	}

	@GetMapping("/updateChef/{idRecipe}")
	public String updateChef(@PathVariable("idRecipe") Long id, Model model) {
		model.addAttribute("recipe",this.recipeService.findById(id));
		model.addAttribute("chefs", this.chefService.findAll());
		return "updateChef.html";
	}

	@GetMapping("/updateIngredients/{idRecipe}")
	public String updateIngredients(Model model, @PathVariable("idRecipe") Long id) {
		Recipe recipe = this.recipeService.findById(id);
		model.addAttribute("recipe", recipe);
		model.addAttribute("ingredients", this.ingredientService.findAvailableIngredients(recipe.getRecipeIngredients()));
		return "updateIngredients.html";
	}
	
	@GetMapping("/setChefToRecipe/{idChef}/{idRecipe}")
	public String setChefToRecipe(Model model, @PathVariable("idChef") Long idChef, @PathVariable("idRecipe") Long idRecipe) {
		Recipe recipe = this.recipeService.findById(idRecipe);
		recipe.setChef(this.chefService.findById(idChef));
		this.recipeService.save(recipe);
		model.addAttribute("recipe", recipe);		
		return "recipe.html";
	}
	
	@GetMapping("/setIngredientToRecipe/{idIngredient}/{idRecipe}")
	public String setIngredientToRecipe(Model model, @PathVariable("idIngredient") Long idIng, @PathVariable("idRecipe") Long idRecipe) {
		Recipe recipe = this.recipeService.findById(idRecipe);		
		this.recipeIngredientService.addIngredient(recipe, this.ingredientService.findById(idIng));
		this.recipeService.save(recipe);
		model.addAttribute("recipe", recipe);
		model.addAttribute("ingredients", this.ingredientService.findAvailableIngredients(recipe.getRecipeIngredients()));
		return "updateIngredients.html";
	}
	
	@GetMapping("/removeIngredientFromRecipe/{idIng}/{idRecipe}")
	public String removeIngredientFromRecipe(Model model,@PathVariable("idIng") Long idIng, @PathVariable("idRecipe") Long idRecipe) {
		Recipe recipe = this.recipeService.findById(idRecipe);
		this.recipeIngredientService.removeIngredient(recipe.getRecipeIngredients(), this.ingredientService.findById(idIng));
		this.recipeService.save(recipe);
		model.addAttribute("recipe", recipe);
		model.addAttribute("ingredients", this.ingredientService.findAvailableIngredients(recipe.getRecipeIngredients()));
		return "updateIngredients.html";
	}
	


}
