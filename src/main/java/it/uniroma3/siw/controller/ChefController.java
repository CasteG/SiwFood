package it.uniroma3.siw.controller;

import java.io.IOException;
import java.util.Set;

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

import it.uniroma3.siw.model.Chef;
import it.uniroma3.siw.model.Image;
import it.uniroma3.siw.model.Recipe;
import it.uniroma3.siw.repository.ImageRepository;
import it.uniroma3.siw.service.ChefService;
import it.uniroma3.siw.service.RecipeService;
import it.uniroma3.siw.validator.ChefValidator;
import jakarta.validation.Valid;

@Controller
public class ChefController {
	
	@Autowired
	private ChefService chefService;
	
	@Autowired
	private ChefValidator chefValidator;
	
	@Autowired
	private ImageRepository imageRepository;

	@Autowired
	private RecipeService recipeService;
	
	@GetMapping("/chef")
	public String showChefs(Model model) {
		model.addAttribute("chefs", chefService.findAll());
		return "chefs.html";
	}
	
	@GetMapping("/chef/{id}")
	public String getChef(@PathVariable("id") Long id, Model model) {
		Set<Recipe> designedRecipes = this.recipeService.findByChefId(id);
		model.addAttribute("designedRecipes", designedRecipes);
		model.addAttribute("chef", chefService.findById(id));
		return "chef.html";
	}
	
	@GetMapping("/admin/formNewChef")
	public String formNewChef(Model model) {
		model.addAttribute("chef", new Chef());
		return "admin/formNewChef.html";
	}
	
	@GetMapping("/admin/manageChefs")
	public String manageChefs(Model model) {
		model.addAttribute("chefs", this.chefService.findAll());
		return "admin/manageChefs.html";
	}
	
	@GetMapping("/admin/removeChef/{id}")
	public String removeChef(@PathVariable("id") Long id, Model model) {
		this.chefService.remove(this.chefService.findById(id));
		return "admin/successfulRemoval.html";
	}
	
	@PostMapping("/chef")
	public String newChef(@Valid @ModelAttribute("chef") Chef chef, 
			BindingResult bindingResult, Model model,
			@RequestParam("file") MultipartFile image) throws IOException {
		this.chefValidator.validate(chef, bindingResult);
		if(!bindingResult.hasErrors()) {
			Image img = new Image(image.getBytes());
			this.imageRepository.save(img);
			chef.setImage(img);
			this.chefService.save(chef);
			model.addAttribute("chef", chef);
			return "redirect:chef/"+chef.getId();
		}
		else 
			model.addAttribute("chefs", this.chefService.findAll());
			return "admin/formNewChef.html";
		
	}
	
}
