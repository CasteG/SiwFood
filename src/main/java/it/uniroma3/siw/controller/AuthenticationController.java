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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Image;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.ImageRepository;
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

	@Autowired
	private ImageRepository imageRepository;
	
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
	                 BindingResult credentialsBindingResult, Model model,
	                 @RequestParam("file") MultipartFile image) throws IOException {

	        // valida user e credenziali
	        this.userValidator.validate(user, userBindingResult);
	        this.credentialsValidator.validate(credentials, credentialsBindingResult);

	        //se entrambi passano la validazione, salvali nel database
	        if(!userBindingResult.hasErrors() && ! credentialsBindingResult.hasErrors()) {
	        	Image img = new Image(image.getBytes());
				this.imageRepository.save(img);
				user.setImage(img);
				//imposta lo User e salva le credenziali
	        	//lo User viene automaticamente salvato grazie a cascade.ALL
	        	//lo chef associato allo User viene salvato automaticamente per lo stesso motivo
				user.getChef().setUser(user);
				user.setCredentials(credentials);
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
	    
	    if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
	        // L'utente non è autenticato
	        return "index.html";
	    } else {
	        // Ottieni il Principal e fai il cast a UserDetails
	        Object principal = authentication.getPrincipal();
	        if (principal instanceof UserDetails) {
	            UserDetails userDetails = (UserDetails) principal;
	            Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());

	            // Verifica il ruolo dell'utente
	            if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
	                // L'utente è un amministratore
	                return "admin/indexAdmin.html";
	            } else {
	                // L'utente è un utente normale
	                return "user/indexUser.html";
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
            return "admin/indexAdmin.html";
        }
    	else if(credentials.getRole().equals(Credentials.DEFAULT_ROLE))
    		return "user/indexUser.html";
    	return "index.html";
    }
	
}
