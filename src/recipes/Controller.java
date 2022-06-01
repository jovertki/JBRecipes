package recipes;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

@RestController
@RequestMapping("/api")
public class Controller {
    Map<Integer, Recipe> recipes = new HashMap<>();//make it a bean
    private final RecipeRepository repository;

    public Controller(RecipeRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/recipe/new")
    @Transactional
    public Object setRecipe(@RequestBody @Valid Recipe r) {

        repository.save(r);
        return new Object(){
            @Getter
            final long id = r.getId();
        };
    }

    @GetMapping("/recipe/{id}")
    public Recipe getRecipe(@PathVariable long id){
        Recipe out = repository.findById(id).orElse(null);

        if (out == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return out;
    }

    @DeleteMapping("/recipe/{id}")
    public ResponseEntity<String> deleteRecipe(@PathVariable long id){
        Optional<Recipe> deletionCandidate = repository.findById(id);

        if (deletionCandidate.isPresent()){
            repository.delete(deletionCandidate.get());
            return ResponseEntity.noContent().build();

        } else {
            return ResponseEntity.notFound().build();
        }
    }

}