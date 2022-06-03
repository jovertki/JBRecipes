package recipes;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api")
public class Controller {
    Map<Integer, Recipe> recipes = new HashMap<>();//make it a bean

    private final RecipeRepository repository;
    private final CategoryRepository categoryRepository;
    public Controller(RecipeRepository repository, CategoryRepository categoryRepository) {
        this.repository = repository;
        this.categoryRepository = categoryRepository;
    }

    @PostMapping("/recipe/new")
    @Transactional
    public Object setRecipe(@RequestBody @Valid Recipe r) {
        categoryRepository.findById(r.getCategoryString())
                .ifPresent(c -> {
                    c.addRecipe(r);
                    r.setCategory(c);
                });
        repository.save(r);
        return new Object(){
            @Getter
            final long id = r.getId();
        };
    }


    private void updateCategory(Recipe oldR, Recipe newR){
        Category oldC = categoryRepository.findById(oldR.getCategoryString()).get();
        oldC.removeRecipe(oldR);
        Optional<Category> newC = categoryRepository.findById(newR.getCategoryString());
        if (newC.isPresent()){
            newC.get().addRecipe(oldR);
            oldR.setCategory(newC.get());
        } else {
            newC = Optional.of(new Category(newR.getCategoryString()));
            categoryRepository.save(newC.get());
            newC.get().addRecipe(oldR);
            oldR.setCategory(newC.get());
        }
    }

    @PutMapping("/recipe/{id}")
    @Transactional
    public ResponseEntity<String> updateRecipe(@PathVariable long id, @RequestBody @Valid Recipe r){
        Optional<Recipe> old = repository.findById(id);

        if (old.isPresent()){
            Recipe oldR = old.get();
            if (!oldR.getCategoryString().equals(r.getCategoryString())){
                updateCategory(oldR, r);
            }
            oldR.update(r);
            repository.save(oldR);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/recipe/{id}")
    public Recipe getRecipe(@PathVariable long id){
        Recipe out = repository.findById(id).orElse(null);

        if (out == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return out;
    }


    @GetMapping("/recipe/search")
    public List<Recipe> searchRecipes(@RequestParam(name = "name", required = false) String name,
                                      @RequestParam(name = "category",required = false) String category){

        if(name == null && category == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } else if (name != null) {
            return repository.findAllByName(name.toLowerCase());
        } else {
            return repository.findAllByCategory(category.toLowerCase());
        }
    }

    @DeleteMapping("/recipe/{id}")
    public ResponseEntity<String> deleteRecipe(@PathVariable long id){
        Optional<Recipe> deletionCandidate = repository.findById(id);

        if (deletionCandidate.isPresent()){
            categoryRepository.findById(deletionCandidate.get().getCategoryString()).get().removeRecipe(deletionCandidate.get());
            repository.delete(deletionCandidate.get());
            return ResponseEntity.noContent().build();

        } else {
            return ResponseEntity.notFound().build();
        }
    }

}