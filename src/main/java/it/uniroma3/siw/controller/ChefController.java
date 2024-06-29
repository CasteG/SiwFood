package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.model.Chef;
import it.uniroma3.siw.service.ChefService;
import it.uniroma3.siw.validator.ChefValidator;
import jakarta.validation.Valid;

@Controller
public class ChefController {
	
	@Autowired
	private ChefService chefService;
	
	@Autowired
	private ChefValidator chefValidator;
	
	@GetMapping("/chef")
	public String showChefs(Model model) {
		model.addAttribute("chefs", chefService.findAll());
		return "chefs.html";
	}
	
	@GetMapping("/chef/{id}")
	public String getChef(@PathVariable("id") Long id, Model model) {
		model.addAttribute("chef", chefService.findById(id));
		return "chef.html";
	}
	
	@GetMapping("/formNewChef")
	public String formNewChef(Model model) {
		model.addAttribute("chef", new Chef());
		return "formNewChef.html";
	}
	
	@PostMapping("/chef")
	public String newChef(@Valid @ModelAttribute("chef") Chef chef, 
			BindingResult bindingResult, Model model) {
		this.chefValidator.validate(chef, bindingResult);
		if(!bindingResult.hasErrors()) {
			this.chefService.save(chef);
			model.addAttribute("chef", chef);
			return "redirect:chef/"+chef.getId();
		}
		else 
			return "formNewChef.html";
		
	}
	
}
