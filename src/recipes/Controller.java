package recipes;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class Controller {
    Recipe recipe;

    @GetMapping("/recipe")
    public Recipe getRecipe(){
        return recipe;
    }

    @PostMapping("/recipe")
    public void setRecipe(@RequestBody Recipe r){
        recipe = r;
    }

}
