package mate.academy.bookstoreprod.repository;

import java.util.List;
import java.util.Optional;
import mate.academy.bookstoreprod.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    List<Book> findAll();

    Optional<Book> findById(Long id);

    Book save(Book book);

    @Modifying
    @Transactional
    @Query("UPDATE Book b SET "
            + "b.title = :#{#book.title}, "
            + "b.author = :#{#book.author}, "
            + "b.isbn = :#{#book.isbn}, "
            + "b.price = :#{#book.price}, "
            + "b.description = :#{#book.description}, "
            + "b.coverImage = :#{#book.coverImage} "
            + "WHERE b.id = :id")
    void updateById(@Param("id") Long id, @Param("book") Book book);

    @Modifying
    @Transactional
    @Query("UPDATE Book b SET b.isDeleted = TRUE WHERE b.id = :id")
    void deleteById(@Param("id") Long id);
}
