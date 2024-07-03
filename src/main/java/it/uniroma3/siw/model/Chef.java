package it.uniroma3.siw.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

@Entity
public class Chef {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotBlank
	private String firstName;

	@NotBlank
	private String lastName;

	@OneToOne
	private Image image;

	@NotNull
	@Past
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private LocalDate dateOfBirth;

	//TO DO: fotorafia

	@OneToMany(mappedBy="chef")
	private Set<Recipe> designedRecipes;

	public Chef() {
		this.designedRecipes = new HashSet<>();
	}
	
	public Image getImage() {
		return image;
	} 
	
	public void setImage(Image image) {
		this.image = image;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public LocalDate getDateOfBirth() {
		return this.dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public Set<Recipe> getDesignedRecipe() {
		return this.designedRecipes;
	}

	public void setDesignedRecipes(Set<Recipe> designedRecipes) {
		this.designedRecipes = designedRecipes;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dateOfBirth, firstName, lastName);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Chef other = (Chef) obj;
		return Objects.equals(dateOfBirth, other.dateOfBirth) && Objects.equals(firstName, other.firstName)
				&& Objects.equals(lastName, other.lastName);
	}

	public Long getId() {
		return this.id;
	}

	@Override
	public String toString() {
		return firstName + " " + lastName;
	}


}
