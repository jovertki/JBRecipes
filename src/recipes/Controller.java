package recipes;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class Controller {
    Map<Integer, Recipe> recipes = new HashMap<>();//make it a bean


    @PostMapping("/recipe/new")
    public Object setRecipe(@RequestBody Recipe r) {
        int i = recipes.size() + 1;
        recipes.put(i, r);
        return new Object(){
            @Getter
            final int id = i;
        };
    }

    @GetMapping("/recipe/{id}")
    public Recipe getRecipe(@PathVariable int id){
        Recipe out = recipes.get(id);
        if (out == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return out;
    }

}
