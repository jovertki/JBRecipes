package recipes;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import javax.validation.Valid;
import java.util.*;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class Controller {
    @Autowired
    private final RecipeRepository recipeRepository;
    @Autowired
    private final CategoryRepository categoryRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private PasswordEncoder encoder;

    @PostMapping("/register")
    @Transactional
    public ResponseEntity<String> addUser(@RequestBody @Valid User user) {
        if (userRepository.existsById(user.getUsername())){
            return ResponseEntity.badRequest().build();
        }

        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/recipe/new")
    @Transactional
    public Object addRecipe(@AuthenticationPrincipal User auth, @RequestBody @Valid Recipe r) {
        categoryRepository.findById(r.getCategoryString())
                .ifPresent(c -> {
                    c.addRecipe(r);
                    r.setCategory(c);
                });
        userRepository.findById(auth.getUsername());
        r.setOwner(userRepository.findById(auth.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST)));
        recipeRepository.save(r);
        return new Object(){
            @Getter
            final long id = r.getId();
        };
    }

    private void updateCategory(Recipe oldR, Recipe newR) {
        Category oldC = categoryRepository.findById(oldR.getCategoryString()).orElse(null);
        assert oldC != null;
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
    public ResponseEntity<String> updateRecipe(@AuthenticationPrincipal User auth,
                                               @PathVariable long id,
                                               @RequestBody @Valid Recipe r){
        Optional<Recipe> old = recipeRepository.findById(id);
        if (old.isPresent()){
            Recipe oldR = old.get();
            if (!oldR.getOwner().getUsername().equals(auth.getUsername())){
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }

            if (!oldR.getCategoryString().equals(r.getCategoryString())){
                updateCategory(oldR, r);
            }
            oldR.update(r);
            recipeRepository.save(oldR);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/recipe/{id}")
    public Recipe getRecipe(@PathVariable long id){
        Recipe out = recipeRepository.findById(id).orElse(null);

        if (out == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return out;
    }

    @GetMapping("/recipe/search")
    public List<Recipe> searchRecipes(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "category",required = false) String category){

        if(name == null && category == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } else if (name != null) {
            return recipeRepository.findAllByName(name.toLowerCase());
        } else {
            return recipeRepository.findAllByCategory(category.toLowerCase());
        }
    }

    @DeleteMapping("/recipe/{id}")
    public ResponseEntity<String> deleteRecipe(@AuthenticationPrincipal User auth,
                                               @PathVariable long id){
        Optional<Recipe> deletionCandidate = recipeRepository.findById(id);

        if (deletionCandidate.isPresent()){
            if (!deletionCandidate.get().getOwner().getUsername().equals(auth.getUsername())){
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
            categoryRepository.findById(deletionCandidate.get().getCategoryString())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR))
                    .removeRecipe(deletionCandidate.get());
            recipeRepository.delete(deletionCandidate.get());
            return ResponseEntity.noContent().build();

        } else {
            return ResponseEntity.notFound().build();
        }
    }

}