package recipes;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.*;




@Entity
@Data
@Table(name = "categories")
@NoArgsConstructor
@EqualsAndHashCode(exclude = "recipesInCategory")
class Category {
    @Id
    @NotBlank
    private String category;

    @OneToMany(mappedBy = "category")
    @JsonIgnore
    private Set<Recipe> recipesInCategory = new HashSet<>();


    public void addRecipe(Recipe r){
        recipesInCategory.add(r);
    }
    public void removeRecipe(Recipe r){
        recipesInCategory.remove(r);
    }

    public Category( String s) {
        category = s;
    }

}

@Entity
@Table(name = "recipes")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private long id;

    @NotBlank
    private String name;

    public void update(Recipe r){
        name = r.name;
        date = LocalDateTime.now();
        description = r.description;
        ingredients = r.ingredients;
        directions = r.directions;
    }


    @NotNull
    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "category")
    @Valid
    @JsonUnwrapped
    private Category  category;

    @JsonIgnore
    public String getCategoryString(){
        return category.getCategory();
    }

    @Getter
    private LocalDateTime date;

    @PreUpdate
    @PrePersist
    public void updateDate(){
        date = LocalDateTime.now();
    }

    @NotBlank
    @Column(name = "description")
    private String description;

    @ElementCollection
    @CollectionTable(name = "ingredients", joinColumns = @JoinColumn(name = "recipe_id"))
    @Size(min = 1)
    private List<String> ingredients = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "directions", joinColumns = @JoinColumn(name = "recipe_id"))
    @Size(min = 1)
    private List<String> directions = new ArrayList<>();



}


