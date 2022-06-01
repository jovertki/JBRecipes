package recipes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "recipes")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Recipe {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private long id;

    @Column(name = "name")
    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @NotBlank
    @Column(name = "description")
    private String description;


    @ElementCollection
    @CollectionTable(name = "ingredients", joinColumns = @JoinColumn(name = "recipe_id"))
    @Column(name = "ingredients")
    @Size(min = 1)
    private List<String> ingredients = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "directions", joinColumns = @JoinColumn(name = "recipe_id"))
    @Column(name = "directions")
    @Size(min = 1)
    private List<String> directions = new ArrayList<>();

//    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
  //  private List<Direction> directions = new ArrayList<>();
}


