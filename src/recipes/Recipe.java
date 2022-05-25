package recipes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class Recipe {
    String name;
    String description;
    String ingredients;
    String directions;
}
