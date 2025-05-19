package mate.academy.bookstoreprod.repository;

import mate.academy.bookstoreprod.repository.category.CategoryRepository;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CategoryRepositoryTest {
    private static final String CATEGORY_NAME = "Travel";
    private static final String NONEXISTING_CATEGORY_NAME = "Nonexistent Category";
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("""
            Category exists by valid name, method returns true
            """)
    @Sql(scripts = {"classpath:database/category/delete-all-from-categories.sql",
            "classpath:database/category/add-one-category.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void existsByName_WithValidName_ReturnsTrue() {
        String name = CATEGORY_NAME;

        boolean exists = categoryRepository.existsByName(name);

        assertTrue(exists, "Expected true for category name: " + name);
    }

    @Test
    @DisplayName("""
            Category does not exist by invalid name, method returns false
            """)
    @Sql(scripts = {"classpath:database/category/delete-all-from-categories.sql",
            "classpath:database/category/add-one-category.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void existsByName_WithInvalidName_ReturnsFalse() {
        String invalidName = NONEXISTING_CATEGORY_NAME;

        boolean exists = categoryRepository.existsByName(invalidName);

        assertFalse(exists, "Expected false for category name: " + invalidName);
    }
}
