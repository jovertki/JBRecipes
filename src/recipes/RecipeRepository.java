package recipes;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecipeRepository extends CrudRepository<Recipe, Long> {

    @Query(value = "Select * from recipes " +
            "where lower(name) LIKE CONCAT('%', :name, '%') " +
            "order by date DESC", nativeQuery = true)
    List<Recipe> findAllByName(@Param("name") String name);
    @Query(value = "Select * from recipes " +
            "where lower(category) LIKE :category " +
            "order by date DESC ", nativeQuery = true)
    List<Recipe> findAllByCategory(@Param("category") String category);

}
