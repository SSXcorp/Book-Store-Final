package mate.academy.bookstoreprod.service;

import java.util.List;
import mate.academy.bookstoreprod.model.Book;

public interface BookService {
    Book save(Book book);

    List<Book> findAll();
}
