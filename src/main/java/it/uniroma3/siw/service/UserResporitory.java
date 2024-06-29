package it.uniroma3.siw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.repository.UserRepository;

@Service
public class UserResporitory {

	@Autowired
	private UserRepository userRepository;
}
