package mate.academy.bookstoreprod.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import mate.academy.bookstoreprod.model.Book;
import mate.academy.bookstoreprod.repository.book.BookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepositoryTest {
    private static final Long ONE = 1L;
    private static final Long THREE = 3L;
    private static final Long TWO = 2L;
    private static final Integer ZERO = 0;
    private static final Integer TEN = 10;

    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("""
            Book can be found by valid Isbn
            """)
    @Sql(scripts = "classpath:database/book/add-one-book.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/book/delete-all-from-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findByIsbn_WithValidIsbn_ReturnsBookOptional() {
        String expectedIsbn = "ISBN1241423456";

        Optional<Book> actualBook = bookRepository.findByIsbn(expectedIsbn);

        assertTrue(actualBook.isPresent(), "Book should be present");
        assertEquals(ONE, actualBook.get().getId(),
                "The ids of books should be the same");
        assertEquals(expectedIsbn, actualBook.get().getIsbn(),
                "The isbn of books should be the same");
    }

    @Test
    @DisplayName("""
            Book cannot be found by invalid Isbn
            """)
    @Sql(scripts = "classpath:database/book/add-one-book.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/book/delete-all-from-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findByIsbn_WithInvalidIsbn_ReturnsEmptyOptional() {
        String invalidIsbn = "ISBN-invalid-12314545";

        Optional<Book> actualBook = bookRepository.findByIsbn(invalidIsbn);

        assertTrue(actualBook.isEmpty(), "Expected to get an empty optional");
    }

    @Test
    @DisplayName("""
            Book exists by valid isbn, method returns true
            """)
    @Sql(scripts = "classpath:database/book/add-one-book.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/book/delete-all-from-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void existsByIsbn_WithValidIsbn_ReturnsTrue() {
        String validIsbn = "ISBN1241423456";

        boolean exists = bookRepository.existsByIsbn(validIsbn);

        assertTrue(exists, "Expected true, but actual is " + exists);
    }

    @Test
    @DisplayName("""
            Book not exists by invalid isbn, method returns false
            """)
    @Sql(scripts = "classpath:database/book/add-one-book.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/book/delete-all-from-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void existsByIsbn_WithInvalidIsbn_ReturnsFalse() {
        String invalidIsbn = "ISBN-invalid-12314545";

        boolean exists = bookRepository.existsByIsbn(invalidIsbn);

        assertFalse(exists, "Expected false, but actual is " + exists);
    }

    @Test
    @DisplayName("""
            Book can be deleted by valid id
            """)
    @Sql(scripts = "classpath:database/book/add-one-book.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/book/delete-all-from-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteById_WithValidId_VoidReturn() {
        bookRepository.deleteById(ONE);

        assertFalse(bookRepository.existsById(ONE),
                "Expected book with id: " + ONE + " to be deleted");
    }

    @Test
    @DisplayName("""
            Find all books pageable
            """)
    @Sql(scripts = "classpath:database/book/add-three-books.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/book/delete-all-from-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAll_WithThreeBooksInDatabase_ReturnsThreeBooks() {
        Page<Book> actual = bookRepository.findAll(PageRequest.of(ZERO, TEN));

        assertEquals(THREE, actual.getTotalElements(),
                "Expected elements with category id 2: 3, but actual: "
                        + actual.getTotalElements());
    }

    @Test
    @DisplayName("""
            Find all books with given valid category
            """)
    @Sql(scripts = {"classpath:database/book/delete-from-books-categories.sql",
                    "classpath:database/book/add-books-with-categories.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/book/delete-all-from-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllByCategories_id_WithValidCategoryId_ReturnsThreeMatchingBooks() {
        Page<Book> actual = bookRepository.findAllByCategories_id(TWO, PageRequest.of(ZERO, TEN));

        assertEquals(THREE, actual.getTotalElements(),
                "Expected elements with category id 2: 3, but actual: "
                        + actual.getTotalElements());
    }
}
