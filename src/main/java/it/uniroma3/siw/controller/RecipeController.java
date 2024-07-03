package it.uniroma3.siw.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Image;
import it.uniroma3.siw.model.Recipe;
import it.uniroma3.siw.repository.ImageRepository;
import it.uniroma3.siw.service.ChefService;
import it.uniroma3.siw.service.CredentialsService;
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

	@Autowired
	private CredentialsService credentialsService;
	
	@GetMapping("/admin/formNewRecipe")
	public String formNewRecipe(Model model) {
		model.addAttribute("recipe", new Recipe());
		model.addAttribute("chefs", this.chefService.findAll());
		return "admin/formNewRecipe.html";
	}

	@PostMapping("/recipe")
	public String newRecipe(@Valid @ModelAttribute("recipe") Recipe recipe, 
			BindingResult bindingResult, Model model,
			@RequestParam("file") MultipartFile image) throws IOException {
		
		this.recipeValidator.validate(recipe, bindingResult);
		if(bindingResult.hasErrors()) {
			model.addAttribute("recipe", recipe);
			model.addAttribute("chefs", this.chefService.findAll());
			return "admin/formNewRecipe.html";
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
	
	@GetMapping("/admin/indexRecipe")
	public String indexRecipe(Model model) {
		return "admin/indexRecipe.html";
	}
	
	@GetMapping("/admin/manageRecipes")
	public String manageRecipes(Model model) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Ottieni il Principal e fai il cast a UserDetails
        Object principal = authentication.getPrincipal();
        // se può accedere a questa risorsa vuol dire che è per forza utente
        UserDetails userDetails = (UserDetails) principal;
        Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
        // se è admin può modificare tutte le ricette
        if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
    		model.addAttribute("recipes", this.recipeService.findAll());
    		return "admin/manageRecipes.html";
        }
        else {	//è user default, puà modificare solo le sue ricette
        	Iterable<Recipe> userRecipes= this.recipeService.findByChef(credentials.getUser().getFirstName(),credentials.getUser().getLastName());
        	model.addAttribute("recipes", userRecipes);
        	return "admin/manageRecipes.html";
        }
	}
	
	@GetMapping("/admin/formUpdateRecipe/{id}")
	public String updateRecipe(@PathVariable("id") Long id, Model model) {
		model.addAttribute("recipe", this.recipeService.findById(id));
		return "admin/formUpdateRecipe.html";
	}
	
	@GetMapping("/admin/removeRecipe/{id}")
	public String removeRecipe(@PathVariable("id") Long id, Model model) {
		this.recipeService.remove(this.recipeService.findById(id));
		return "admin/successfulRemoval.html";
	}

	@GetMapping("/admin/updateChef/{idRecipe}")
	public String updateChef(@PathVariable("idRecipe") Long id, Model model) {
		model.addAttribute("recipe",this.recipeService.findById(id));
		model.addAttribute("chefs", this.chefService.findAll());
		return "admin/updateChef.html";
	}

	@GetMapping("/admin/updateIngredients/{idRecipe}")
	public String updateIngredients(Model model, @PathVariable("idRecipe") Long id) {
		Recipe recipe = this.recipeService.findById(id);
		model.addAttribute("recipe", recipe);
		model.addAttribute("ingredients", this.ingredientService.findAvailableIngredients(recipe.getRecipeIngredients()));
		return "admin/updateIngredients.html";
	}
	
	@GetMapping("/admin/setChefToRecipe/{idChef}/{idRecipe}")
	public String setChefToRecipe(Model model, @PathVariable("idChef") Long idChef, @PathVariable("idRecipe") Long idRecipe) {
		Recipe recipe = this.recipeService.findById(idRecipe);
		recipe.setChef(this.chefService.findById(idChef));
		this.recipeService.save(recipe);
		model.addAttribute("recipe", recipe);		
		return "recipe.html";
	}
	
	@GetMapping("/admin/setIngredientToRecipe/{idIngredient}/{idRecipe}")
	public String setIngredientToRecipe(Model model, @PathVariable("idIngredient") Long idIng, @PathVariable("idRecipe") Long idRecipe) {
		Recipe recipe = this.recipeService.findById(idRecipe);		
		this.recipeIngredientService.addIngredient(recipe, this.ingredientService.findById(idIng));
		this.recipeService.save(recipe);
		model.addAttribute("recipe", recipe);
		model.addAttribute("ingredients", this.ingredientService.findAvailableIngredients(recipe.getRecipeIngredients()));
		return "admin/updateIngredients.html";
	}
	
	@GetMapping("/admin/removeIngredientFromRecipe/{idIng}/{idRecipe}")
	public String removeIngredientFromRecipe(Model model,@PathVariable("idIng") Long idIng, @PathVariable("idRecipe") Long idRecipe) {
		Recipe recipe = this.recipeService.findById(idRecipe);
		this.recipeIngredientService.removeIngredient(recipe.getRecipeIngredients(), this.ingredientService.findById(idIng));
		this.recipeService.save(recipe);
		model.addAttribute("recipe", recipe);
		model.addAttribute("ingredients", this.ingredientService.findAvailableIngredients(recipe.getRecipeIngredients()));
		return "admin/updateIngredients.html";
	}
	


}
