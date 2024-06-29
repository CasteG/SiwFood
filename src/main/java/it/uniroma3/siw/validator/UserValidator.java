package it.uniroma3.siw.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.User;

@Component
public class UserValidator implements Validator {

    final Integer MAX_NAME_LENGTH = 30;
    final Integer MIN_NAME_LENGTH = 4;

    @Override
    public void validate(Object o, Errors errors) {
        User user = (User) o;
        String firstName = user.getFirstName().trim();
        String lastName = user.getLastName().trim();

        if (firstName.isEmpty())
            errors.reject("user.emptyFirstName");
        else if (firstName.length() < MIN_NAME_LENGTH || firstName.length() > MAX_NAME_LENGTH)
            errors.reject("user.incorrectLengthFirstName");

        if (lastName.isEmpty())
            errors.reject("user.emptyLastName");
        else if (lastName.length() < MIN_NAME_LENGTH || lastName.length() > MAX_NAME_LENGTH)
            errors.reject("user.incorrectLengthLastName");
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

}

