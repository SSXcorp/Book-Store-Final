package mate.academy.bookstoreprod.repository.book;

import java.util.Optional;
import mate.academy.bookstoreprod.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {

    boolean existsByIsbn(String isbn);

    Page<Book> findAllByCategories_id(Long categoryId, Pageable pageable);

    Optional<Book> findByIsbn(String isbn);

    @EntityGraph(attributePaths = {"categories"})
    Page<Book> findAll(Pageable pageable);
}
