package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.validator.CredentialsValidator;
import it.uniroma3.siw.validator.UserValidator;
import jakarta.validation.Valid;

@Controller
public class AuthenticationController {

	@Autowired
	private CredentialsService credentialsService;

	@Autowired
	private UserValidator userValidator;

	@Autowired
	private CredentialsValidator credentialsValidator;
	
	@GetMapping("/register")
	public String showRegisterForm(Model model) {
		model.addAttribute("user", new User());
		model.addAttribute("credentials", new Credentials());
		
		return "formRegisterUser.html";
	}
	
	 @PostMapping("/register")
	    public String registerUser(@Valid @ModelAttribute("user") User user,
	                 BindingResult userBindingResult,
	                 @Valid @ModelAttribute("credentials") Credentials credentials,
	                 BindingResult credentialsBindingResult,
	                 Model model) {

	        // valida user e credenziali
	        this.userValidator.validate(user, userBindingResult);
	        this.credentialsValidator.validate(credentials, credentialsBindingResult);

	        //se entrambi passano la validazione, salvali nel database
	        if(!userBindingResult.hasErrors() && ! credentialsBindingResult.hasErrors()) {
	        	//imposta lo User e salva le credenziali
	        	//lo User viene automaticamente salvato grazie a cascade.ALL
	            credentials.setUser(user);
	            credentialsService.saveCredentials(credentials);
	            model.addAttribute("user",user);
	            return "registrationSuccessful.html";
	        }
	        model.addAttribute("user", user);
	        model.addAttribute("credentials", credentials);
	        return "formRegisterUser.html";
	    }
	
	@GetMapping("/login")
	public String showLoginForm(Model model) {
		return "formLogin.html";
	}
	
	@GetMapping("/login/error")
	public String showLoginErrorForm(Model model) {
		/* passo alla form un messaggio di errore */
		String errorMessage = new String("Username o password incorretti");
		model.addAttribute("errorMessage", errorMessage);
		return "formLogin.html";
	}
	
	@GetMapping("/logout")
	public String logout(Model model) {
		return "index.html";
	}
	
	@GetMapping("/")
	public String index(Model model) {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    if (authentication instanceof AnonymousAuthenticationToken) {
	        return "index.html";
	    } else {
	        // Ottieni il Principal e fai il cast a UserDetails
	        Object principal = authentication.getPrincipal();
	        if (principal instanceof UserDetails) {
	            UserDetails userDetails = (UserDetails) principal;
	            Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
	            if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
	                //return "admin/indexAdmin.html";
	            	return "index.html";
	            }
	        }
	    }
	    return "index.html";
	}

	
	@GetMapping("/success")
    public String defaultAfterLogin(Model model) {
    	UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
    	if(credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
            return "index.html";
        }
        return "index.html";
    }
	
}
