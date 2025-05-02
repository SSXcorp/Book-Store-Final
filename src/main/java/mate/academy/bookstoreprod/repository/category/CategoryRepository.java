package mate.academy.bookstoreprod.repository.category;

import mate.academy.bookstoreprod.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByName(String name);
}
