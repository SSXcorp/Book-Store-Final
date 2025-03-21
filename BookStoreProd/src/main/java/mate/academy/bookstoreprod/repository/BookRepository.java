package mate.academy.bookstoreprod.repository;

import java.util.List;
import mate.academy.bookstoreprod.model.Book;

public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();
}
