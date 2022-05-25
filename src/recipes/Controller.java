package recipes;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
    Recipe recipe;

    @GetMapping("/api/recipe")
    public Recipe getRecipe(){
        return recipe;
    }

    @PostMapping("/api/recipe")
    public void setRecipe(@RequestBody Recipe r){
        recipe = r;
    }

}
