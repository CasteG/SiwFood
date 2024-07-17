package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.service.CredentialsService;

@ControllerAdvice
public class GlobalController {
	
	@Autowired
	private CredentialsService credentialsService;

	  @ModelAttribute("userDetails")
	   public UserDetails getUser() {
	    UserDetails user = null;
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    if (!(authentication instanceof AnonymousAuthenticationToken)) {
	      user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    }
	    return user;
	  }
	

		@ModelAttribute("role")
		public String getUserRole(){
			try {
				UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
				return credentials.getRole();
			}catch(ClassCastException e){
				return null;
			}
		}
}
