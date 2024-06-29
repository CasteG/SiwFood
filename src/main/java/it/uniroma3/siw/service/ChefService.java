package it.uniroma3.siw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Chef;
import it.uniroma3.siw.repository.ChefRepository;

@Service
public class ChefService {
	
	@Autowired
	private ChefRepository chefRepository;

	public Iterable<Chef> findAll() {
		return this.chefRepository.findAll();
	}

	public Chef findById(Long id) {
		return this.chefRepository.findById(id).get();
	}

	public void save(Chef chef) {
		this.chefRepository.save(chef);
	}

}
