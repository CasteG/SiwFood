package it.uniroma3.siw.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.service.CredentialsService;

@Component
public class CredentialsValidator implements Validator {
	
	@Autowired
    private CredentialsService credentialsService;

    final Integer MAX_USERNAME_LENGTH = 20;
    final Integer MIN_USERNAME_LENGTH = 4;
    final Integer MAX_PASSWORD_LENGTH = 20;
    final Integer MIN_PASSWORD_LENGTH = 4;

    @Override
    public void validate(Object o, Errors errors) {
        Credentials credentials = (Credentials) o;
        String username = credentials.getUsername().trim();
        String password = credentials.getPassword().trim();

        if (username.isEmpty())
            errors.reject("credentials.emptyUsername");
        else if (username.length() < MIN_USERNAME_LENGTH || username.length() > MAX_USERNAME_LENGTH)
            errors.reject("credentials.incorrectUsernameLength");
        else if (this.credentialsService.existsByUsername(username))
            errors.reject("credentials.duplicate");

        if (password.isEmpty())
            errors.reject("credentials.emptyPassword");
        else if (password.length() < MIN_PASSWORD_LENGTH || password.length() > MAX_PASSWORD_LENGTH)
            errors.reject("credentials.incorrectPasswordLength");
    }
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Credentials.class.equals(clazz);
	}

}
