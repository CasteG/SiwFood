package it.uniroma3.siw.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Recipe {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotBlank
	private String name;
	
	@Column(length = 2000)
	private String description;
	
	@OneToOne
	private Image image;

	@ManyToOne
	@NotNull
	private Chef chef;
	
	@OneToMany(mappedBy="recipe", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<RecipeIngredient> recipeIngredients;
	
	public Recipe() {
		this.recipeIngredients = new HashSet<>();
	}
	
	public Image getImage() {
		return image;
	} 
	
	public void setImage(Image image) {
		this.image = image;
	}
	
	public Long getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Chef getChef() {
		return this.chef;
	}

	public void setChef(Chef chef) {
		this.chef = chef;
	}

	public Set<RecipeIngredient> getRecipeIngredients() {
		return this.recipeIngredients;
	}

	public void setRecipeIngredients(Set<RecipeIngredient> ingredients) {
		this.recipeIngredients = ingredients;
	}

	@Override
	public int hashCode() {
		return Objects.hash(recipeIngredients, name, chef);
	}
	

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Recipe other = (Recipe) obj;
		return Objects.equals(recipeIngredients, other.recipeIngredients) && Objects.equals(name, other.name)
				&& Objects.equals(chef, other.chef);
	}
	


	
}



