package it.uniroma3.siw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;

	public boolean existsByFirstNameAndLastName(String firstName, String lastName) {
		return this.userRepository.existsByFirstNameAndLastName(firstName, lastName);
	}

}
